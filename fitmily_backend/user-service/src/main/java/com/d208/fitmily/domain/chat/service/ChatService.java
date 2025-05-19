package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.chat.dto.ChatMessageDTO;
import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChatMessageService chatMessageService;

    /**
     * 채팅 메시지 목록 조회
     */
    public ChatMessagesResponseDTO getMessages(String familyId, String userId, String before, int limit) {
        log.info("메시지 조회 - familyId: {}, userId: {}, before: {}, limit: {}",
                familyId, userId, before, limit);

        // 메시지 조회 (MongoDB)
        List<ChatMessage> messages;
        boolean hasMore = false;

        try {
            if (before != null && !before.isEmpty()) {
                try {
                    // 메시지 ID에서 타임스탬프 추출 (타임스탬프_familyId 형식)
                    String[] parts = before.split("_");
                    long timestamp = Long.parseLong(parts[0]);
                    Date beforeDate = new Date(timestamp);

                    messages = messageRepository.findByFamilyIdAndSentAtBeforeOrderBySentAtDesc(
                            familyId, beforeDate, limit + 1);
                } catch (Exception e) {
                    log.error("타임스탬프 파싱 오류, 최신 메시지 조회로 전환: {}", e.getMessage());
                    messages = messageRepository.findByFamilyIdOrderBySentAtDesc(familyId, limit + 1);
                }
            } else {
                messages = messageRepository.findByFamilyIdOrderBySentAtDesc(familyId, limit + 1);
            }

            // 추가 메시지 존재 여부 확인
            if (messages.size() > limit) {
                hasMore = true;
                messages = messages.subList(0, limit);
            }

            // 메시지 조회 시 자동으로 읽음 처리
            if (!messages.isEmpty() && userId != null) {
                String latestMessageId = messages.get(0).getMessageId();

                // 가족 확인 후 읽음 처리
                if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                    chatMessageService.markAsRead(familyId, latestMessageId, userId);
                }
            }
        } catch (Exception e) {
            log.error("메시지 조회 오류", e);
            messages = List.of(); // 빈 목록 반환
        }

        // DTO 변환
        List<ChatMessageDTO> messageDTOs = messages.stream()
                .map(this::convertToChatMessageDTO)
                .collect(Collectors.toList());

        return new ChatMessagesResponseDTO(messageDTOs, hasMore);
    }

    /**
     * ChatMessage 엔티티를 DTO로 변환
     */
    private ChatMessageDTO convertToChatMessageDTO(ChatMessage message) {
        ChatMessageDTO.SenderInfoDTO senderInfoDTO = null;
        if (message.getSenderInfo() != null) {
            senderInfoDTO = ChatMessageDTO.SenderInfoDTO.builder()
                    .nickname(message.getSenderInfo().getNickname())
                    .familySequence(message.getSenderInfo().getFamilySequence())
                    .build();
        }

        ChatMessageDTO.MessageContentDTO contentDTO = null;
        if (message.getContent() != null) {
            contentDTO = ChatMessageDTO.MessageContentDTO.builder()
                    .messageType(message.getContent().getMessageType())
                    .text(message.getContent().getText())
                    .imageUrl(message.getContent().getImageUrl())
                    .build();
        }

        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .senderId(message.getSenderId())
                .senderInfo(senderInfoDTO)
                .content(contentDTO)
                .sentAt(message.getSentAt())
                .readByUserIds(message.getReadByUserIds())
                .unreadCount(message.getUnreadCount())
                .build();
    }

    /**
     * 안 읽은 메시지 수 조회
     */
    public int getUnreadCount(String familyId, String userId) {
        log.info("안 읽은 메시지 수 조회 - familyId: {}, userId: {}", familyId, userId);

        try {
            // Redis에서 안 읽은 메시지 수 조회
            String unreadCountStr = (String) redisTemplate.opsForValue().get("unread:" + familyId + ":" + userId);
            if (unreadCountStr != null) {
                try {
                    return Integer.parseInt(unreadCountStr);
                } catch (NumberFormatException e) {
                    log.warn("안 읽은 메시지 수 파싱 실패: {}", unreadCountStr);
                }
            }

            // Redis에 없으면 MongoDB에서 조회
            return (int) messageRepository.countUnreadMessages(familyId, userId);
        } catch (Exception e) {
            log.error("안 읽은 메시지 수 조회 오류", e);
            return 0;
        }
    }
}