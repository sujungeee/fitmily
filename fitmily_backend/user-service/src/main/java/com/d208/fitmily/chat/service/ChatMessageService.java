package com.d208.fitmily.chat.service;

import com.d208.fitmily.chat.dto.MessageRequestDTO;
import com.d208.fitmily.chat.dto.ReadReceiptResponseDTO;
import com.d208.fitmily.chat.entity.ChatMessage;
import com.d208.fitmily.chat.repository.MessageRepository;
import com.d208.fitmily.common.exception.BusinessException;
import com.d208.fitmily.common.exception.ErrorCode;
import com.d208.fitmily.family.mapper.FamilyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final FamilyMapper familyMapper;

    public void sendMessage(String familyId, MessageRequestDTO messageRequest, String userId) {
        // 메시지 유효성 검증
        if (!StringUtils.hasText(messageRequest.getContent()) &&
                ("text".equals(messageRequest.getMessageType()))) {
            throw new BusinessException(ErrorCode.CHAT_MESSAGE_SEND_FAILED);
        }

        // 채팅방 접근 권한 확인
        if (!familyMapper.checkFamilyMembership(familyId, userId)) {
            throw new BusinessException(ErrorCode.CHAT_ACCESS_DENIED);
        }

        // 메시지 ID 생성 - 타임스탬프 기반으로 수정
        String messageId = System.currentTimeMillis() + "_" + familyId;

        // 가족 구성원 수 조회
        int familyMembersCount = familyMapper.countFamilyMembers(familyId);

        // 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setFamilyId(familyId);
        chatMessage.setSenderId(userId);

        // 발신자 정보 설정 - 실제로는 사용자 서비스에서 조회
        ChatMessage.SenderInfo senderInfo = new ChatMessage.SenderInfo();
        senderInfo.setNickname("사용자_" + userId);
        senderInfo.setFamilySequence("2");
        chatMessage.setSenderInfo(senderInfo);

        // 메시지 컨텐츠 설정
        ChatMessage.MessageContent content = new ChatMessage.MessageContent();
        content.setMessageType(messageRequest.getMessageType());
        content.setText(messageRequest.getContent());
        content.setImageUrl(messageRequest.getImageUrl());
        chatMessage.setContent(content);

        // 메시지 시간 및 읽음 상태 설정
        chatMessage.setSentAt(new Date());
        chatMessage.setReadByUserIds(Collections.singletonList(userId));
        chatMessage.setUnreadCount(familyMembersCount - 1);

        // MongoDB에 메시지 저장
        messageRepository.save(chatMessage);

        // Redis에 발신자의 마지막 읽은 메시지 ID 업데이트
        try {
            // 발신자의 마지막 읽은 메시지 ID 업데이트
            redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, messageId);

            // 가족 구성원의 안 읽은 메시지 수 증가
            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);
            for (String memberId : familyMembers) {
                if (!memberId.equals(userId)) {
                    // 기존 안 읽은 메시지 수 조회
                    String unreadCountStr = (String) redisTemplate.opsForValue().get("unread:" + familyId + ":" + memberId);
                    int unreadCount = 0;
                    if (unreadCountStr != null) {
                        unreadCount = Integer.parseInt(unreadCountStr);
                    }
                    // 안 읽은 메시지 수 증가
                    redisTemplate.opsForValue().set("unread:" + familyId + ":" + memberId, String.valueOf(unreadCount + 1));
                }
            }
        } catch (Exception e) {
            // Redis 오류 무시하고 계속 진행
            System.out.println("Redis 작업 실패 (무시됨): " + e.getMessage());
        }

        // WebSocket 브로드캐스트 - 응답 형식 수정
        Map<String, Object> response = new HashMap<>();
        response.put("type", "CHAT_MESSAGE");
        response.put("data", chatMessage);
        messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, response);

        // 오프라인 사용자 푸시 알림
        sendNotificationsToOfflineUsers(chatMessage, familyId, userId);
    }

    // 메시지 읽음 상태 업데이트 메서드
    public void markAsRead(String familyId, String messageId, String userId) {
        // 채팅방 접근 권한 확인
        if (!familyMapper.checkFamilyMembership(familyId, userId)) {
            throw new BusinessException(ErrorCode.CHAT_ACCESS_DENIED);
        }

        try {
            // Redis에 마지막 읽은 메시지 ID 업데이트
            redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, messageId);

            // 안 읽은 메시지 수를 0으로 리셋
            redisTemplate.opsForValue().set("unread:" + familyId + ":" + userId, "0");

            // MongoDB 읽음 상태 업데이트 (해당 메시지보다 이전 메시지 모두 읽음 처리)
            messageRepository.updateReadStatusBeforeId(familyId, messageId, userId);

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
        } catch (Exception e) {
            System.out.println("읽음 상태 업데이트 실패: " + e.getMessage());
            throw new BusinessException(ErrorCode.CHAT_MESSAGE_READ_FAILED);
        }
    }

    // 오프라인 사용자에게 알림 전송
    private void sendNotificationsToOfflineUsers(ChatMessage message, String familyId, String userId) {
        try {
            // 가족 구성원 목록 조회
            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);

            // 오프라인 상태인 사용자 확인 및 알림 전송
            for (String memberId : familyMembers) {
                if (!memberId.equals(userId)) {
                    // Redis에서 사용자의 온라인 상태 확인
                    Boolean isOnline = redisTemplate.hasKey("user:online:" + memberId);

                    // 오프라인 상태면 푸시 알림 전송
                    if (Boolean.FALSE.equals(isOnline)) {
                        // FCM 알림 전송 로직 구현
                        System.out.println("FCM 알림 전송 (미구현): 사용자 " + memberId +
                                "에게 메시지 알림: " + message.getContent().getText());
                    }
                }
            }
        } catch (Exception e) {
            // 예외 무시하고 로그만 출력
            System.out.println("알림 전송 실패 (무시됨): " + e.getMessage());
        }
    }
}