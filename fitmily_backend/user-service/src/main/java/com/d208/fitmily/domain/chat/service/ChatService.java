package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.chat.dto.ChatMessageDTO;
import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import com.d208.fitmily.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatMessageService chatMessageService;
    private final UserService userService;

    // ChatMessage 엔티티를 DTO로 변환
    private ChatMessageDTO convertToChatMessageDTO(ChatMessage message) {
        if (message == null) {
            return null;
        }

        ChatMessageDTO.SenderInfoDTO senderInfoDTO = null;
        if (message.getSenderInfo() != null) {
            String nickname = message.getSenderInfo().getNickname() != null ?
                    message.getSenderInfo().getNickname() : "사용자_" + message.getSenderId();

            // null인 경우 기본값 설정
            String familySequence = message.getSenderInfo().getFamilySequence() != null ?
                    message.getSenderInfo().getFamilySequence() : "0";
            String userZodiacName = message.getSenderInfo().getUserZodiacName() != null ?
                    message.getSenderInfo().getUserZodiacName() : "Unknown";

            senderInfoDTO = ChatMessageDTO.SenderInfoDTO.builder()
                    .nickname(nickname)
                    .familySequence(familySequence)
                    .userZodiacName(userZodiacName)
                    .build();
        } else {
            senderInfoDTO = ChatMessageDTO.SenderInfoDTO.builder()
                    .nickname("사용자_" + message.getSenderId())
                    .familySequence("0")
                    .userZodiacName("Unknown")
                    .build();
        }

        // MessageContent 처리 - null 값 방지
        ChatMessageDTO.MessageContentDTO contentDTO = null;
        if (message.getContent() != null) {
            contentDTO = ChatMessageDTO.MessageContentDTO.builder()
                    .messageType(message.getContent().getMessageType())
                    .text(message.getContent().getText())
                    .imageUrl(message.getContent().getImageUrl())
                    .build();
        } else {
            // Content가 null인 경우 기본값 생성
            contentDTO = ChatMessageDTO.MessageContentDTO.builder()
                    .messageType("text")
                    .text("")
                    .imageUrl(null)
                    .build();
        }

        return ChatMessageDTO.builder()
                .messageId(message.getMessageId())
                .familyId(message.getFamilyId())
                .senderId(message.getSenderId())
                .senderInfo(senderInfoDTO)
                .content(contentDTO)
                .sentAt(message.getSentAt())
                .readByUserIds(message.getReadByUserIds() != null ?
                        message.getReadByUserIds() : Collections.emptyList())
                .unreadCount(message.getUnreadCount())
                .build();
    }

    public ChatMessagesResponseDTO getMessagesByPage(String familyId, String userId, int page, int limit) {
        try {
            log.info("페이지 메시지 조회 - familyId: {}, userId: {}, page: {}, limit: {}",
                    familyId, userId, page, limit);

            // 페이지네이션 (skip = page * limit)
            int skip = page * limit;
            List<ChatMessage> messages = messageRepository.findByFamilyIdWithPagination(familyId, skip, limit + 1);

            // 추가 메시지 존재 여부 확인
            boolean hasMore = false;
            if (messages.size() > limit) {
                hasMore = true;
                messages = messages.subList(0, limit);
            }

            // 첫 페이지 조회 시 자동으로 읽음 처리
            if (page == 0 && !messages.isEmpty() && userId != null) {
                String latestMessageId = messages.get(0).getMessageId();
                if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                    chatMessageService.markAsRead(familyId, latestMessageId, userId);
                }
            }

            // 메시지의 발신자 정보 업데이트
            updateMessagesSenderInfo(messages);

            // DTO 변환
            List<ChatMessageDTO> messageDTOs = messages.stream()
                    .map(this::convertToChatMessageDTO)
                    .collect(Collectors.toList());

            return new ChatMessagesResponseDTO(messageDTOs, hasMore);
        } catch (Exception e) {
            log.error("메시지 조회 중 오류 발생", e);
            // 예외를 던지는 대신 빈 응답 반환
            return new ChatMessagesResponseDTO(Collections.emptyList(), false);
        }
    }

    // 메시지 목록의 발신자 정보 업데이트
    private void updateMessagesSenderInfo(List<ChatMessage> messages) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        Set<String> senderIds = messages.stream()
                .map(ChatMessage::getSenderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 각 발신자별로 사용자 정보 조회 및 메시지 업데이트
        for (String senderId : senderIds) {
            try {
                Integer senderIdInt = Integer.parseInt(senderId);
                com.d208.fitmily.domain.walk.dto.UserDto userDto = userService.getUserDtoById(senderIdInt);

                if (userDto != null) {
                    // 해당 발신자의 모든 메시지 업데이트
                    for (ChatMessage message : messages) {
                        if (senderId.equals(message.getSenderId())) {
                            if (message.getSenderInfo() == null) {
                                message.setSenderInfo(new ChatMessage.SenderInfo());
                            }

                            message.getSenderInfo().setNickname(userDto.getUserNickname());
                            message.getSenderInfo().setFamilySequence(String.valueOf(userDto.getUserFamilySequence()));
                            message.getSenderInfo().setUserZodiacName(userDto.getUserZodiacName());
                        }
                    }

                    log.debug("발신자 정보 업데이트 완료: senderId={}, nickname={}, zodiac={}",
                            senderId, userDto.getUserNickname(), userDto.getUserZodiacName());
                }
            } catch (Exception e) {
                log.warn("발신자 정보 업데이트 실패: senderId={}, error={}", senderId, e.getMessage());
            }
        }
    }

    // 사용자 정보 업데이트 메서드 추가
    private void updateSenderInfo(ChatMessage message) {
        if (message == null || message.getSenderId() == null) {
            return;
        }

        try {
            // senderId를 정수로 변환
            Integer senderIdInt = Integer.parseInt(message.getSenderId());

            // UserService를 통해 사용자 정보 조회
            com.d208.fitmily.domain.walk.dto.UserDto userDto = userService.getUserDtoById(senderIdInt);

            if (userDto != null) {
                // SenderInfo가 null인 경우 새로 생성
                if (message.getSenderInfo() == null) {
                    message.setSenderInfo(new ChatMessage.SenderInfo());
                }

                // 실제 DB 값으로 업데이트
                message.getSenderInfo().setNickname(userDto.getUserNickname());
                message.getSenderInfo().setFamilySequence(String.valueOf(userDto.getUserFamilySequence()));
                message.getSenderInfo().setUserZodiacName(userDto.getUserZodiacName());

                log.debug("사용자 정보 업데이트: userId={}, nickname={}, familySequence={}, zodiac={}",
                        message.getSenderId(), userDto.getUserNickname(),
                        userDto.getUserFamilySequence(), userDto.getUserZodiacName());
            }
        } catch (Exception e) {
            log.warn("사용자 정보 업데이트 실패: senderId={}, error={}", message.getSenderId(), e.getMessage());
        }
    }
}