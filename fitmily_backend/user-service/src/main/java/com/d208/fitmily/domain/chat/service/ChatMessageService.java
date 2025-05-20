package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
import com.d208.fitmily.domain.chat.dto.ReadReceiptResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.domain.fcm.service.FcmService;
import com.d208.fitmily.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final FamilyMapper familyMapper;
    private final FcmService fcmService;
    private final UserService userService;

    // 패밀리 접근 권한 검증
    public boolean validateUserFamilyAccess(String userId, String familyId) {
        try {
            if (userId == null) {
                return false;
            }

            // 가족 구성원 목록 조회
            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);
            return familyMembers.contains(userId);
        } catch (Exception e) {
            log.error("권한 검증 오류", e);
            return false;
        }
    }

    // 메시지 전송
    public void sendMessage(String familyId, MessageRequestDTO messageRequest, String userId) {
        log.info("메시지 전송 시작 - familyId: {}, userId: {}, type: {}",
                familyId, userId, messageRequest.getMessageType());

        try {
            // 텍스트 메시지 내용 검증
            if (!StringUtils.hasText(messageRequest.getContent()) &&
                    "text".equals(messageRequest.getMessageType())) {
                throw new IllegalArgumentException("텍스트 메시지 내용이 없습니다");
            }

            // 메시지 ID 생성 (타임스탬프_familyId 형식)
            String messageId = System.currentTimeMillis() + "_" + familyId;

            // 가족 구성원 수 조회
            int familyMembersCount = familyMapper.countFamilyMembers(familyId);

            // 메시지 엔티티 생성
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setMessageId(messageId);
            chatMessage.setFamilyId(familyId);
            chatMessage.setSenderId(userId);

            // 발신자 정보 설정
            ChatMessage.SenderInfo senderInfo = new ChatMessage.SenderInfo();
            senderInfo.setNickname("사용자_" + userId);

            try {
                // userId를 정수로 변환
                Integer userIdInt = Integer.parseInt(userId);

                // UserDto를 사용하여 사용자 정보 조회
                com.d208.fitmily.domain.walk.dto.UserDto userDto = userService.getUserDtoById(userIdInt);

                if (userDto != null) {
                    // UserDto에서 필요한 정보 설정
                    senderInfo.setNickname(userDto.getUserNickname());
                    senderInfo.setFamilySequence(String.valueOf(userDto.getUserFamilySequence()));
                    senderInfo.setUserZodiacName(userDto.getUserZodiacName());

                    log.debug("사용자 정보 설정 완료: nickname={}, familySequence={}, zodiacName={}",
                            userDto.getUserNickname(), userDto.getUserFamilySequence(), userDto.getUserZodiacName());
                } else {
                    log.warn("사용자 정보를 찾을 수 없음: userId={}", userId);
                }
            } catch (Exception e) {
                log.warn("사용자 정보 조회 실패: {}", e.getMessage());
            }

            chatMessage.setSenderInfo(senderInfo);

            // 메시지 내용 설정
            ChatMessage.MessageContent content = new ChatMessage.MessageContent();
            content.setMessageType(messageRequest.getMessageType());
            content.setText(messageRequest.getContent());
            content.setImageUrl(messageRequest.getImageUrl());
            chatMessage.setContent(content);

            // 읽음 상태 초기화 (발신자는 이미 읽음)
            List<String> readByUserIds = new ArrayList<>();
            readByUserIds.add(userId);
            chatMessage.setSentAt(new Date());
            chatMessage.setReadByUserIds(readByUserIds);
            chatMessage.setUnreadCount(familyMembersCount - 1);  // 발신자 제외

            // MongoDB에 저장
            chatMessage = messageRepository.save(chatMessage);
            log.info("메시지 저장 완료: {}", messageId);

            // Redis에 읽음 상태 업데이트 (발신자)
            redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, messageId);

            // 온라인 사용자 목록에 발신자 추가
            redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", userId);

            // 웹소켓으로 메시지 브로드캐스트
            Map<String, Object> response = new HashMap<>();
            response.put("type", "CHAT_MESSAGE");
            response.put("data", chatMessage);

            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, response);
            log.info("메시지 브로드캐스트 완료: /topic/chat/family/{}", familyId);

            // 오프라인 사용자에게 FCM 알림 전송
            sendFCMNotificationsToOfflineUsers(familyId, chatMessage);

        } catch (Exception e) {
            log.error("메시지 전송 오류", e);
            throw new RuntimeException("메시지 전송 실패: " + e.getMessage(), e);
        }
    }

    // 오프라인 사용자에게 FCM 알림 전송
    // ChatMessageService.java에서 기존 sendFCMNotificationsToOfflineUsers 메서드 수정
    private void sendFCMNotificationsToOfflineUsers(String familyId, ChatMessage message) {
        try {
            // 1. 가족 구성원 목록 조회
            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);

            // 2. 온라인 사용자 목록 정확하게 가져오기
            Set<Object> onlineUsers = redisTemplate.opsForSet().members("family:" + familyId + ":subscribers");
            Set<String> onlineUserIds = new HashSet<>();

            if (onlineUsers != null) {
                for (Object user : onlineUsers) {
                    onlineUserIds.add(user.toString());
                }
            }

            log.debug("채팅방 온라인 사용자: {}", onlineUserIds);

            // 3. 오프라인 사용자 필터링 (발신자 제외)
            List<String> offlineUsers = familyMembers.stream()
                    .filter(userId -> !userId.equals(message.getSenderId()) && !onlineUserIds.contains(userId))
                    .collect(Collectors.toList());

            log.info("FCM 알림 대상: {}", offlineUsers);

            if (offlineUsers.isEmpty()) {
                log.debug("오프라인 사용자가 없음, FCM 알림 생략");
                return;
            }

            // 4. 메시지 발신자 정보 조회
            String senderName = message.getSenderInfo() != null ?
                    message.getSenderInfo().getNickname() : "사용자_" + message.getSenderId();

            // 5. 메시지 내용
            String messageContent = "";
            String messageType = "TEXT";
            if (message.getContent() != null) {
                messageType = message.getContent().getMessageType();
                if ("text".equals(messageType)) {
                    messageContent = message.getContent().getText();
                } else if ("image".equals(messageType)) {
                    messageContent = "이미지를 보냈습니다";
                }
            }

            // 6. FCM 알림 전송
            fcmService.sendChatMessageNotification(
                    senderName, messageContent, familyId, message.getMessageId(),
                    message.getSenderId(), messageType, message.getSentAt(), offlineUsers);

            log.info("오프라인 사용자 {} 명에게 FCM 알림 전송 완료", offlineUsers.size());
        } catch (Exception e) {
            log.error("FCM 알림 전송 중 오류: {}", e.getMessage(), e);
        }
    }

    // 메시지 읽음 처리
    public void markAsRead(String familyId, String messageId, String userId) {
        log.info("읽음 처리 시작 - familyId: {}, messageId: {}, userId: {}",
                familyId, messageId, userId);

        try {
            // Redis에 읽음 상태 업데이트
            redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, messageId);
            redisTemplate.opsForValue().set("unread:" + familyId + ":" + userId, "0");

            // MongoDB 읽음 상태 일괄 업데이트
            int updatedCount = messageRepository.updateReadStatusBeforeId(familyId, messageId, userId);
            log.debug("읽음 처리 완료: {} 개 메시지", updatedCount);

            // 읽음 상태 이벤트 브로드캐스트
            ReadReceiptResponseDTO readReceipt = ReadReceiptResponseDTO.builder()
                    .type("READ_RECEIPT")
                    .data(ReadReceiptResponseDTO.ReadReceiptData.builder()
                            .userId(userId)
                            .messageId(messageId)
                            .timestamp(new Date())
                            .build())
                    .build();

            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, readReceipt);
            log.info("읽음 상태 브로드캐스트 완료: {}", messageId);

        } catch (Exception e) {
            log.error("읽음 처리 오류", e);
            throw new RuntimeException("읽음 처리 실패: " + e.getMessage(), e);
        }
    }
}