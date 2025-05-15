package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.chat.dto.ChatMessageDTO;
import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import com.d208.fitmily.global.common.exception.BusinessException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final FamilyMapper familyMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 채팅 메시지 목록 조회
     * @param familyId 가족 ID
     * @param userId 사용자 ID
     * @param before 특정 시간 이전의 메시지 조회 (페이징)
     * @param limit 한 번에 조회할 메시지 수
     * @return 메시지 목록과 추가 메시지 존재 여부
     */
    public ChatMessagesResponseDTO getMessages(String familyId, String userId, String before, int limit) {
        // 접근 권한 확인
        if (!familyMapper.checkFamilyMembership(familyId, userId)) {
            throw new BusinessException(ErrorCode.CHAT_ACCESS_DENIED);
        }

        // 메시지 조회
        List<ChatMessage> messages;
        if (before != null && !before.isEmpty()) {
            Date beforeDate = new Date(Long.parseLong(before));
            messages = messageRepository.findByFamilyIdAndSentAtBeforeOrderBySentAtDesc(familyId, beforeDate, limit);
        } else {
            messages = messageRepository.findByFamilyIdOrderBySentAtDesc(familyId, limit);
        }

        // 추가 메시지 존재 여부 확인
        boolean hasMore = false;
        if (!messages.isEmpty()) {
            Date oldestMsgDate = messages.get(messages.size() - 1).getSentAt();
            long olderCount = messageRepository.countByFamilyIdAndSentAtBefore(familyId, oldestMsgDate);
            hasMore = olderCount > 0;
        }

        // 읽음 처리
        if (!messages.isEmpty()) {
            String newestMessageId = messages.get(0).getMessageId();

            try {
                // 현재 조회된 메시지 중 가장 최신 메시지 ID를 마지막 읽은 메시지로 설정
                redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, newestMessageId);

                // 안 읽은 메시지 수 초기화
                redisTemplate.opsForValue().set("unread:" + familyId + ":" + userId, "0");

                // 읽음 처리 업데이트
                List<String> messageIds = messages.stream()
                        .map(ChatMessage::getMessageId)
                        .collect(Collectors.toList());

                processReadReceipts(messageIds, userId, familyId);

            } catch (Exception e) {
                log.error("Redis 읽음 상태 업데이트 실패: {}", e.getMessage());
            }
        }

        // DTO 변환
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ChatMessagesResponseDTO(messageDTOs, hasMore);
    }

    /**
     * 메시지 읽음 처리
     * @param messageIds 읽은 메시지 ID 목록
     * @param userId 사용자 ID
     * @param familyId 가족 ID
     */
    private void processReadReceipts(List<String> messageIds, String userId, String familyId) {
        if (messageIds.isEmpty()) return;

        try {
            // MongoDB 읽음 상태 업데이트
            messageRepository.updateReadStatus(messageIds, userId, familyId);

            // 읽음 상태 변경 브로드캐스트
            messagingTemplate.convertAndSend(
                    "/topic/chat/family/" + familyId + "/read",
                    messageIds
            );
        } catch (Exception e) {
            log.error("읽음 처리 실패: {}", e.getMessage());
        }
    }

    /**
     * 모델 DTO 변환
     */
    private ChatMessageDTO convertToDTO(ChatMessage message) {
        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .senderInfo(convertSenderInfoToDTO(message.getSenderInfo()))
                .content(convertContentToDTO(message.getContent()))
                .sentAt(message.getSentAt())
                .readByUserIds(message.getReadByUserIds())
                .unreadCount(message.getUnreadCount())
                .build();
    }

    private ChatMessageDTO.SenderInfoDTO convertSenderInfoToDTO(ChatMessage.SenderInfo senderInfo) {
        if (senderInfo == null) return null;

        return ChatMessageDTO.SenderInfoDTO.builder()
                .nickname(senderInfo.getNickname())
                .familySequence(senderInfo.getFamilySequence())
                .build();
    }

    private ChatMessageDTO.MessageContentDTO convertContentToDTO(ChatMessage.MessageContent content) {
        if (content == null) return null;

        return ChatMessageDTO.MessageContentDTO.builder()
                .messageType(content.getMessageType())
                .text(content.getText())
                .imageUrl(content.getImageUrl())
                .build();
    }

    /**
     * 안 읽은 메시지 수 조회
     * @param familyId 가족 ID
     * @param userId 사용자 ID
     * @return 안 읽은 메시지 수
     */
    public int getUnreadCount(String familyId, String userId) {
        try {
            String unreadCountStr = (String) redisTemplate.opsForValue().get("unread:" + familyId + ":" + userId);
            return unreadCountStr != null ? Integer.parseInt(unreadCountStr) : 0;
        } catch (Exception e) {
            log.error("안 읽은 메시지 수 조회 실패: {}", e.getMessage());
            return 0;
        }
    }
}