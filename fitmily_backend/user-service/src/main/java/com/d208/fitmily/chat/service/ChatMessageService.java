package com.d208.fitmily.chat.service;

import com.d208.fitmily.chat.dto.MessageRequestDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final MessageRepository messageRepository;
//    private final MentionService mentionService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final FamilyMapper familyMapper; // 더미 매퍼 사용

    public void sendMessage(String familyId, MessageRequestDTO messageRequest, String userId) {
        // 메시지 유효성 검증
        if (!StringUtils.hasText(messageRequest.getContent()) &&
                ("text".equals(messageRequest.getType()))) {
            throw new BusinessException(ErrorCode.CHAT_MESSAGE_SEND_FAILED);
        }

        // 채팅방 접근 권한 확인
        if (!familyMapper.checkFamilyMembership(familyId, userId)) {
            throw new BusinessException(ErrorCode.CHAT_ACCESS_DENIED);
        }

        // 메시지 ID 생성
        String messageId = "msg_" + UUID.randomUUID().toString().replace("-", "");

//        // 멘션 처리
//        List<ChatMessage.Mention> mentions = new ArrayList<>();
//        if ("text".equals(messageRequest.getType()) && messageRequest.getContent().contains("@")) {
//            mentions = mentionService.extractMentions(messageRequest.getContent(), familyId);
//        }

        // 가족 구성원 수 조회
        int familyMembersCount = familyMapper.countFamilyMembers(familyId);

        // 메시지 객체 생성
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessageId(messageId);
        chatMessage.setSenderId(userId);

        // 발신자 정보 설정 (간소화)
        ChatMessage.SenderInfo senderInfo = new ChatMessage.SenderInfo();
        senderInfo.setNickname("사용자_" + userId); // 간소화된 구현
        senderInfo.setProfileColor("#FF5733");
        senderInfo.setProfileImageUrl(null);
        chatMessage.setSenderInfo(senderInfo);

        ChatMessage.MessageContent content = new ChatMessage.MessageContent();
        content.setType(messageRequest.getType());
        content.setText(messageRequest.getContent());
        content.setImageUrl(null);
//        content.setMentions(mentions);
        chatMessage.setContent(content);

        chatMessage.setSentAt(new Date());
        chatMessage.setReadByUserIds(Collections.singletonList(userId));
        chatMessage.setUnreadCount(familyMembersCount - 1);

        // MongoDB에 메시지 저장
        messageRepository.save(chatMessage);

        // WebSocket 브로드캐스트
        messagingTemplate.convertAndSend("/topic/chat/family/" + familyId, chatMessage);

        // 오프라인 사용자 푸시 알림 (FCM 없이 구현)
        sendNotificationsToOfflineUsers(chatMessage, familyId, userId);
    }

    // 메서드 수정
    private void sendNotificationsToOfflineUsers(ChatMessage message, String familyId, String userId) {
        try {
            // 가족 구성원 목록 조회
            List<String> familyMembers = familyMapper.selectFamilyMemberIds(familyId);

            // 더미 구현으로 변경
            final Set<String> onlineUsers = new HashSet<>();

            // 오프라인 사용자 = 모든 사용자 (간소화)
            List<String> offlineUsers = familyMembers.stream()
                    .filter(id -> !id.equals(userId))
                    .collect(Collectors.toList());

            // 로그만 출력
            System.out.println("알림 전송 대상 오프라인 사용자 (더미): " + offlineUsers);
            System.out.println("메시지 내용: " + message.getContent().getText());
            System.out.println("발신자: " + message.getSenderInfo().getNickname());
        } catch (Exception e) {
            // 예외 무시하고 로그만 출력
            System.out.println("알림 전송 실패 (무시됨): " + e.getMessage());
        }
    }
}