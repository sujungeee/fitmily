//package com.d208.fitmily.domain.chat.service;
//
//import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
//import com.d208.fitmily.domain.chat.dto.ReadReceiptResponseDTO;
//import com.d208.fitmily.domain.chat.entity.ChatMessage;
//import com.d208.fitmily.domain.chat.repository.MessageRepository;
//import com.d208.fitmily.domain.family.mapper.FamilyMapper;
//import com.d208.fitmily.domain.fcm.entity.Fcm;
//import com.d208.fitmily.domain.fcm.service.FcmService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//
//    private final MessageRepository messageRepository;
//    private final RedisTemplate<String, Object> redisTemplate;
//    private final SimpMessagingTemplate messagingTemplate;
//    private final FamilyMapper familyMapper;
//    private final FcmService fcmService;
//
//    /**
//     * 사용자가 특정 패밀리에 접근할 권한이 있는지 확인
//     */
//    public boolean validateUserFamilyAccess(String userId, String familyId) {
//        try {
//            log.debug("사용자-패밀리 권한 확인: userId={}, familyId={}", userId, familyId);
//
//            // CustomUserDetails 객체 처리
//            if (userId != null && userId.contains("CustomUserDetails")) {
//                log.warn("CustomUserDetails 형식의 userId 처리 - 접근 거부: {}", userId);
//                return false;
//            }
//
//            if (userId != null) {
//                try {
//
//                    Integer userIdInt = Integer.parseInt(userId);
//
//                    List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);
//
//                    boolean isMember = familyMembers.contains(userId);
//                    log.debug("패밀리 구성원 확인 결과: {}", isMember);
//                    return isMember;
//                } catch (NumberFormatException e) {
//                    log.error("사용자 ID {} 숫자 변환 실패: {}", userId, e.getMessage());
//                    return false;
//                }
//            }
//
//            return false;
//        } catch (Exception e) {
//            log.error("사용자-패밀리 관계 확인 중 오류", e);
//            return false;
//        }
//    }
//
//    public void sendMessage(String familyId, MessageRequestDTO messageRequest, String userId) {
//        log.info("메시지 전송 시작 - familyId: {}, userId: {}, type: {}",
//                familyId, userId, messageRequest.getMessageType());
//
//        try {
//            // 메시지 유효성 검증
//            if (!StringUtils.hasText(messageRequest.getContent()) &&
//                    "text".equals(messageRequest.getMessageType())) {
//                log.error("빈 텍스트 메시지");
//                throw new IllegalArgumentException("텍스트 메시지 내용이 없습니다");
//            }
//
//            // 메시지 ID 생성 (타임스탬프_familyId 형식)
//            String messageId = System.currentTimeMillis() + "_" + familyId;
//
//            // 읽음 상태 초기화 (발신자는 이미 읽음)
//            List<String> readByUserIds = new ArrayList<>();
//            readByUserIds.add(userId);
//
//            // 가족 구성원 수 조회
//            int familyMembersCount = 1; // 기본값으로 초기화 (최소 발신자 1명)
//            try {
//                familyMembersCount = familyMapper.countFamilyMembers(familyId);
//            } catch (Exception e) {
//                log.error("가족 구성원 수 조회 중 오류 발생", e);
//            }
//
//            // 현재 시간
//            Date sentAt = new Date();
//
//            // 메시지 엔티티 생성
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setMessageId(messageId);
//            chatMessage.setFamilyId(familyId);
//            chatMessage.setSenderId(userId);
//
//            // 발신자 정보 설정 (사용자 정보 조회)
//            ChatMessage.SenderInfo senderInfo = new ChatMessage.SenderInfo();
//            try {
//                // 사용자 정보 조회 로직 (닉네임, 가족 순서 등)
//                senderInfo.setNickname("사용자_" + userId);
//            } catch (Exception e) {
//                log.warn("사용자 정보 조회 실패, 기본값 사용: {}", e.getMessage());
//                senderInfo.setNickname("사용자_" + userId);
//                senderInfo.setFamilySequence("?");
//            }
//            chatMessage.setSenderInfo(senderInfo);
//
//            // 메시지 내용 설정
//            ChatMessage.MessageContent content = new ChatMessage.MessageContent();
//            content.setMessageType(messageRequest.getMessageType());
//            content.setText(messageRequest.getContent());
//            content.setImageUrl(messageRequest.getImageUrl());
//            chatMessage.setContent(content);
//
//            chatMessage.setSentAt(sentAt);
//            chatMessage.setReadByUserIds(readByUserIds);
//            chatMessage.setUnreadCount(Math.max(familyMembersCount - 1, 0));  // 발신자 제외
//
//            // MongoDB에 저장
//            chatMessage = messageRepository.save(chatMessage);
//            log.info("메시지 저장 완료: {}", messageId);
//
//            // Redis에 데이터 업데이트
//            updateRedisAfterMessageSend(familyId, messageId, userId);
//
//            // 웹소켓으로 메시지 브로드캐스트
//            Map<String, Object> response = new HashMap<>();
//            response.put("type", "CHAT_MESSAGE");
//            response.put("data", chatMessage);
//
//            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, response);
//            log.info("메시지 브로드캐스트 완료: /topic/chat/family/{}", familyId);
//
//            // 오프라인 사용자에게 FCM 알림 전송
//            sendFCMNotificationsToOfflineUsers(familyId, chatMessage);
//
//        } catch (Exception e) {
//            log.error("메시지 전송 중 오류 발생", e);
//            throw new RuntimeException("메시지 전송 실패: " + e.getMessage(), e);
//        }
//    }
//
//    /**
//     * 오프라인 사용자에게 FCM 알림 전송
//     */
//    private void sendFCMNotificationsToOfflineUsers(String familyId, ChatMessage message) {
//        try {
//            // 1. 가족 구성원 목록 조회
//            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);
//
//            // 2. 현재 구독 중인 사용자 목록 조회 (온라인 사용자)
//            Set<Object> onlineUsers = redisTemplate.opsForSet().members("family:" + familyId + ":subscribers");
//            Set<String> onlineUserIds = new HashSet<>();
//            if (onlineUsers != null) {
//                onlineUsers.forEach(userId -> onlineUserIds.add(userId.toString()));
//            }
//
//            // 3. 오프라인 사용자 필터링 (발신자는 제외)
//            List<String> offlineUsers = familyMembers.stream()
//                    .filter(userId -> !userId.equals(message.getSenderId()) && !onlineUserIds.contains(userId))
//                    .collect(Collectors.toList());
//
//            if (offlineUsers.isEmpty()) {
//                log.debug("오프라인 사용자가 없습니다. FCM 알림 전송 생략");
//                return;
//            }
//
//            // 4. 메시지 발신자 정보 조회
//            String senderName = message.getSenderInfo() != null ?
//                    message.getSenderInfo().getNickname() : "사용자_" + message.getSenderId();
//
//            // 5. 메시지 내용 (텍스트 또는 이미지 설명)
//            String messageContent = "";
//            String messageType = "TEXT";
//            if (message.getContent() != null) {
//                messageType = message.getContent().getMessageType();
//                if ("text".equals(messageType)) {
//                    messageContent = message.getContent().getText();
//                } else if ("image".equals(messageType)) {
//                    messageContent = "이미지를 보냈습니다";
//                }
//            }
//
//            // 6. FCM 알림 전송
//            fcmService.sendChatMessageNotification(
//                    senderName, messageContent, familyId, message.getMessageId(),
//                    message.getSenderId(), messageType, message.getSentAt(), offlineUsers);
//
//            log.info("오프라인 사용자 {} 명에게 FCM 알림 전송 완료", offlineUsers.size());
//
//        } catch (Exception e) {
//            log.error("FCM 알림 전송 중 오류 발생: {}", e.getMessage());
//            // 알림 전송 실패가 메시지 기능에 영향을 주지 않도록 예외를 캐치하고 로그만 남김
//        }
//    }
//
//    /**
//     * Redis 데이터 업데이트 - 메시지 전송 후
//     * 1. 발신자의 마지막 읽은 메시지 ID 업데이트
//     * 2. 가족 구성원의 안 읽은 메시지 수 증가
//     */
//    private void updateRedisAfterMessageSend(String familyId, String messageId, String senderId) {
//        try {
//            // 1. 발신자의 마지막 읽은 메시지 ID 업데이트
//            redisTemplate.opsForValue().set("read:" + familyId + ":" + senderId, messageId);
//
//            // 2. 가족 구성원의 안 읽은 메시지 수 증가 (발신자 제외)
//            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);
//
//            for (String memberId : familyMembers) {
//                if (!memberId.equals(senderId)) {
//                    // 기존 안 읽은 메시지 수 조회
//                    String unreadCountStr = (String) redisTemplate.opsForValue().get("unread:" + familyId + ":" + memberId);
//                    int unreadCount = 0;
//
//                    if (unreadCountStr != null) {
//                        try {
//                            unreadCount = Integer.parseInt(unreadCountStr);
//                        } catch (NumberFormatException e) {
//                            log.warn("안 읽은 메시지 수 파싱 실패: {}", unreadCountStr);
//                        }
//                    }
//
//                    // 안 읽은 메시지 수 증가
//                    redisTemplate.opsForValue().set("unread:" + familyId + ":" + memberId, String.valueOf(unreadCount + 1));
//                    log.debug("안 읽은 메시지 수 증가: familyId={}, userId={}, count={}", familyId, memberId, unreadCount + 1);
//                }
//            }
//
//            // 3. 채팅방에 현재 구독 중인 사용자 목록 관리 (온라인 상태)
//            redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", senderId);
//
//        } catch (Exception e) {
//            log.warn("Redis 업데이트 실패 (계속 진행): {}", e.getMessage());
//        }
//    }
//
//    public void markAsRead(String familyId, String messageId, String userId) {
//        log.info("메시지 읽음 처리 - familyId: {}, messageId: {}, userId: {}",
//                familyId, messageId, userId);
//
//        try {
//            // Redis에 마지막 읽은 메시지 ID 업데이트
//            redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, messageId);
//            redisTemplate.opsForValue().set("unread:" + familyId + ":" + userId, "0");
//            log.debug("Redis 읽음 상태 업데이트 - 안 읽은 메시지 수: 0");
//
//            // MongoDB 읽음 상태 업데이트
//            int updatedCount = messageRepository.updateReadStatusBeforeId(familyId, messageId, userId);
//            log.debug("MongoDB 읽음 상태 업데이트 - 업데이트된 메시지 수: {}", updatedCount);
//
//            // 읽음 상태 이벤트 브로드캐스트
//            ReadReceiptResponseDTO readReceipt = ReadReceiptResponseDTO.builder()
//                    .type("READ_RECEIPT")
//                    .data(ReadReceiptResponseDTO.ReadReceiptData.builder()
//                            .userId(userId)
//                            .messageId(messageId)
//                            .timestamp(new Date())
//                            .build())
//                    .build();
//
//            messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, readReceipt);
//            log.info("읽음 상태 브로드캐스트 완료: {}", messageId);
//
//        } catch (Exception e) {
//            log.error("메시지 읽음 처리 중 오류 발생", e);
//            throw new RuntimeException("메시지 읽음 처리 실패: " + e.getMessage(), e);
//        }
//    }
//}

package com.d208.fitmily.domain.chat.service;

import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
import com.d208.fitmily.domain.chat.dto.ReadReceiptResponseDTO;
import com.d208.fitmily.domain.chat.entity.ChatMessage;
import com.d208.fitmily.domain.chat.repository.MessageRepository;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.domain.fcm.service.FcmService;
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
    private final ChatSessionService chatSessionService;

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
            senderInfo.setNickname("사용자_" + userId);  // 실제로는 사용자 정보 조회 로직 필요
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