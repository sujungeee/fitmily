//package com.d208.fitmily.fcm.service;
//
//import com.d208.fitmily.chat.entity.ChatMessage;
//import com.d208.fitmily.common.exception.BusinessException;
//import com.d208.fitmily.common.exception.ErrorCode;
//import com.d208.fitmily.fcm.dto.FCMTokenDTO;
//import com.d208.fitmily.fcm.mapper.UserDeviceMapper;
//import com.d208.fitmily.user.dto.UserProfileDTO;
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class FCMService {
//
//    private final FirebaseMessaging firebaseMessaging;
//    private final UserDeviceMapper userDeviceMapper;
//
//    public void sendChatNotifications(List<String> userIds, ChatMessage message, UserProfileDTO sender) {
//        try {
//            // FCM 토큰 조회
//            List<FCMTokenDTO> tokens = userDeviceMapper.selectActiveFCMTokensByUserIds(userIds);
//
//            if (tokens.isEmpty()) {
//                return;
//            }
//
//            // 알림 데이터 구성
//            Map<String, String> data = new HashMap<>();
//            data.put("type", "CHAT");
//            data.put("id", message.getMessageId());
//            data.put("senderId", message.getSenderId());
//            data.put("senderName", sender.getNickname());
//            data.put("familyChatId", message.getFamilyId());
//            data.put("message", message.getContent().getText());
//            data.put("messageType", message.getContent().getType().toUpperCase());
//            data.put("timestamp", message.getSentAt().toString());
//
//            // 각 토큰별로 FCM 메시지 전송
//            for (FCMTokenDTO token : tokens) {
//                Message fcmMessage = Message.builder()
//                        .setToken(token.getToken())
//                        .setNotification(Notification.builder()
//                                .setTitle(sender.getNickname())
//                                .setBody(message.getContent().getText())
//                                .build())
//                        .putAllData(data)
//                        .build();
//
//                firebaseMessaging.send(fcmMessage);
//            }
//        } catch (Exception e) {
//            throw new BusinessException(ErrorCode.FCM_NOTIFICATION_FAILED);
//        }
//    }
//}