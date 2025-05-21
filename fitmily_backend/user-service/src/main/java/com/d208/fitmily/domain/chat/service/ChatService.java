package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.AwsS3.Service.AwsS3Service;
import com.d208.fitmily.domain.chat.dto.ChatMessageDTO;
import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import com.d208.fitmily.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final AwsS3Service awsS3Service;

    // ChatMessage 엔티티를 DTO로 변환 (기존 코드 유지)
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
            String imageUrl = message.getContent().getImageUrl();

            // 이미지 타입이고 이미지 URL이 있는 경우 presigned URL로 변환
            if ("image".equals(message.getContent().getMessageType())
                    && imageUrl != null && !imageUrl.isBlank()) {
                imageUrl = awsS3Service.generatePresignedDownloadUrl(imageUrl);
            }

            contentDTO = ChatMessageDTO.MessageContentDTO.builder()
                    .messageType(message.getContent().getMessageType())
                    .text(message.getContent().getText())
                    .imageUrl(imageUrl)
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
            // 페이지네이션 (최신 메시지부터 페이지 단위로 조회)
            int skip = page * limit;

            // 메시지 조회 (최신 메시지부터 정렬)
            List<ChatMessage> messages = messageRepository.findByFamilyIdWithPagination(familyId, skip, limit + 1);

            // 추가 메시지 존재 여부 확인
            boolean hasMore = false;
            if (messages.size() > limit) {
                messages = messages.subList(0, limit);
                hasMore = true;
            }

            // 첫 페이지(최신 메시지)일 때만 읽음 처리
            if (page == 0 && !messages.isEmpty() && userId != null) {
                // 첫 번째 메시지(가장 최신 메시지)의 ID로 읽음 처리
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

            // 리스트 뒤집기 (최신 메시지가 맨 아래에 오도록)
            Collections.reverse(messageDTOs);

            return new ChatMessagesResponseDTO(messageDTOs, hasMore);
        } catch (Exception e) {
            log.error("메시지 조회 중 오류 발생: {}", e.getMessage(), e);
            return new ChatMessagesResponseDTO(Collections.emptyList(), false);
        }
    }

    // 메시지 목록의 발신자 정보 업데이트 (코드 유지)
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
                }
            } catch (Exception e) {
                log.warn("발신자 정보 업데이트 실패: senderId={}, error={}", senderId, e.getMessage());
            }
        }
    }
}