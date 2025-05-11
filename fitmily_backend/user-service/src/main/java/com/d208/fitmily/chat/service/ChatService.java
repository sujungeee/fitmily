package com.d208.fitmily.chat.service;

import com.d208.fitmily.chat.dto.ChatMessageDTO;
import com.d208.fitmily.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.chat.entity.ChatMessage;
import com.d208.fitmily.chat.repository.MessageRepository;
import com.d208.fitmily.common.exception.BusinessException;
import com.d208.fitmily.common.exception.ErrorCode;
import com.d208.fitmily.family.mapper.FamilyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final FamilyMapper familyMapper; // 더미 매퍼 사용
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessagesResponseDTO getMessages(String familyId, String userId, String before, int limit) {
        // 접근 권한 확인
        if (!familyMapper.checkFamilyMembership(familyId, userId)) {
            throw new BusinessException(ErrorCode.CHAT_ACCESS_DENIED);
        }

        // 메시지 조회
        List<ChatMessage> messages;
        if (before != null && !before.isEmpty()) {
            Date beforeDate = new Date(before);
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
        List<String> messageIds = messages.stream()
                .map(ChatMessage::getMessageId)
                .collect(Collectors.toList());

        processReadReceipts(messageIds, userId, familyId);

        // DTO 변환
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new ChatMessagesResponseDTO(messageDTOs, hasMore);
    }

    // Redis 작업 예외 처리 메서드 추가
    private void safeRedisOperation(Runnable operation) {
        try {
            operation.run();
        } catch (Exception e) {
            // Redis 오류 무시하고 계속 진행
            System.out.println("Redis 작업 실패 : " + e.getMessage());
        }
    }

    // 메서드 내부에서 Redis 호출 부분 수정
    private void processReadReceipts(List<String> messageIds, String userId, String familyId) {
        if (messageIds.isEmpty()) return;

        // MongoDB 읽음 상태 업데이트
        messageRepository.updateReadStatus(messageIds, userId, familyId);

        // 읽음 상태 변경 브로드캐스트 (Redis 오류 무시)
        try {
            messagingTemplate.convertAndSend(
                    "/topic/chat/family/" + familyId + "/read",
                    messageIds
            );
        } catch (Exception e) {
            System.out.println("WebSocket 브로드캐스트 실패 (무시됨): " + e.getMessage());
        }
    }

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
                .profileColor(senderInfo.getProfileColor())
                .profileImageUrl(senderInfo.getProfileImageUrl())
                .build();
    }

    private ChatMessageDTO.MessageContentDTO convertContentToDTO(ChatMessage.MessageContent content) {
        if (content == null) return null;

//        List<ChatMessageDTO.MentionDTO> mentions = null;
//        if (content.getMentions() != null) {
//            mentions = content.getMentions().stream()
//                    .map(mention -> ChatMessageDTO.MentionDTO.builder()
//                            .userId(mention.getUserId())
//                            .nickname(mention.getNickname())
//                            .startIndex(mention.getStartIndex())
//                            .endIndex(mention.getEndIndex())
//                            .build())
//                    .collect(Collectors.toList());
//        }

        return ChatMessageDTO.MessageContentDTO.builder()
                .type(content.getType())
                .text(content.getText())
                .imageUrl(content.getImageUrl())
                .build();
    }
}