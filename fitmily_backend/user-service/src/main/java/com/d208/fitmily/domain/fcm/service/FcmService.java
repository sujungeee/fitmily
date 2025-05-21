package com.d208.fitmily.domain.fcm.service;

import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.domain.fcm.dto.FcmTokenDTO;
import com.d208.fitmily.domain.fcm.entity.Fcm;
import com.d208.fitmily.domain.fcm.mapper.FcmMapper;
import com.d208.fitmily.domain.notification.mapper.NotificationMapper;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.user.mapper.UserMapper;
import com.d208.fitmily.domain.walk.dto.UserDto;
import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class FcmService {

    @Autowired
    private FamilyMapper familyMapper;
    private final FcmMapper fcmMapper;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;
    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FcmService(FcmMapper fcmMapper,
                      UserMapper userMapper,
                      NotificationMapper notificationMapper,
                      FirebaseMessaging firebaseMessaging) {
        this.fcmMapper = fcmMapper;
        this.userMapper = userMapper;
        this.notificationMapper = notificationMapper;
        this.firebaseMessaging = firebaseMessaging;
    }

    /**
     * FCM 토큰 등록
     */
    @Transactional
    public void registerToken(int userId, String fcmToken) {
        log.info("FCM 토큰 등록: userId={}, token={}", userId, fcmToken);

        // 유저 존재 확인
        User user = userMapper.selectById(userId);
        if (user == null) {
            log.error("사용자를 찾을 수 없습니다: userId={}", userId);
            throw new RuntimeException("사용자를 찾을 수 없습니다: " + userId);
        }

        // 기존 토큰 확인
        Fcm existingToken = fcmMapper.findByUserIdAndFcmToken(userId, fcmToken);

        LocalDateTime now = LocalDateTime.now();

        if (existingToken != null) {
            // 기존 토큰 업데이트
            existingToken.setFcmUpdatedAt(now);
            fcmMapper.updateFcmToken(existingToken);
            log.info("기존 FCM 토큰 업데이트 완료: {}", existingToken.getFcmId());
        } else {
            // 새 토큰 등록
            Fcm fcm = new Fcm();
            fcm.setUserId(userId);
            fcm.setFcmToken(fcmToken);
            fcm.setFcmCreatedAt(now);
            fcm.setFcmUpdatedAt(now);
            fcmMapper.insertFcmToken(fcm);
            log.info("새 FCM 토큰 등록 완료: {}", fcm.getFcmId());
        }
    }

    /**
     * 콕 찌르기 알림 전송
     */
    public void sendPokeNotification(UserDto sender, UserDto target, int pokeId, LocalDateTime timestamp) {
        log.info("콕 찌르기 알림 전송: sender={}, target={}, pokeId={}",
                sender.getUserId(), target.getUserId(), pokeId);

        // 대상 사용자의 FCM 토큰 조회
        List<Fcm> targetTokens = fcmMapper.findByUserId(target.getUserId());

        if (targetTokens.isEmpty()) {
            log.warn("대상 사용자의 FCM 토큰이 없음: userId={}", target.getUserId());
            return;
        }

        for (Fcm fcm : targetTokens) {
            try {
                // 알림 메시지 생성
                Message message = createPokeNotificationMessage(sender, pokeId, timestamp, fcm.getFcmToken());

                // 메시지 전송
                String response = firebaseMessaging.send(message);
                log.info("콕 찌르기 알림 전송 성공: {}", response);
            } catch (FirebaseMessagingException e) {
                log.error("콕 찌르기 알림 전송 실패: token={}, error={}", fcm.getFcmToken(), e.getMessage());
                // 유효하지 않은 토큰 처리
                if (isInvalidTokenError(e)) {
                    fcmMapper.deleteFcmToken(fcm.getFcmId());
                    log.info("유효하지 않은 FCM 토큰 삭제: {}", fcm.getFcmToken());
                }
            }
        }
    }

    /**
     * 콕 찌르기 알림 메시지 생성
     */
    private Message createPokeNotificationMessage(UserDto sender, int pokeId, LocalDateTime timestamp, String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "POKE");
        data.put("id", String.valueOf(pokeId));
        data.put("senderId", String.valueOf(sender.getUserId()));

        String senderName = sender.getUserNickname() != null ? sender.getUserNickname() : "사용자";
        data.put("senderName", senderName);

        data.put("timestamp", timestamp.format(DateTimeFormatter.ISO_DATE_TIME));

        // 알림 정보
        com.google.firebase.messaging.Notification notification = com.google.firebase.messaging.Notification.builder()
                .setTitle("콕 찌르기")
                .setBody(senderName + "님이 당신에게 산책을 장려했어요!")
                .build();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("poke_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 챌린지 종료 알림 전송
     */
    public void sendChallengeCompletionNotification(int familyId, int challengeId, String startDate,
                                                    String endDate, int targetDistance, float totalDistance) {
        log.info("챌린지 종료 알림 전송: familyId={}, challengeId={}", familyId, challengeId);

        // 가족 구성원의 FCM 토큰 조회
        List<FcmTokenDTO> familyTokens = fcmMapper.findTokensByFamilyId(familyId);

        if (familyTokens.isEmpty()) {
            log.warn("가족 구성원의 FCM 토큰이 없음: familyId={}", familyId);
            return;
        }

        // 달성율 계산
        int achievementRate = (int) ((totalDistance / targetDistance) * 100);

        for (FcmTokenDTO tokenDto : familyTokens) {
            try {
                // 알림 메시지 생성
                Message message = createChallengeCompletionNotificationMessage(
                        challengeId, startDate, endDate, targetDistance, totalDistance, achievementRate, tokenDto.getToken());

                // 메시지 전송
                String response = firebaseMessaging.send(message);
                log.info("챌린지 종료 알림 전송 성공: userId={}, response={}", tokenDto.getUserId(), response);
            } catch (FirebaseMessagingException e) {
                log.error("챌린지 종료 알림 전송 실패: userId={}, token={}, error={}",
                        tokenDto.getUserId(), tokenDto.getToken(), e.getMessage());
                if (isInvalidTokenError(e)) {
                    Fcm fcm = fcmMapper.findByUserIdAndFcmToken(tokenDto.getUserId(), tokenDto.getToken());
                    if (fcm != null) {
                        fcmMapper.deleteFcmToken(fcm.getFcmId());
                    }
                }
            }
        }
    }

    /**
     * 챌린지 알림 메시지 생성
     */
    private Message createChallengeNotificationMessage(int challengeId, String startDate,
                                                       String endDate, int targetDistance, String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "CHALLENGE");
        data.put("id", String.valueOf(challengeId));
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // 알림 정보
        Notification notification = Notification.builder()
                .setTitle("새로운 주간 산책 챌린지가 시작되었습니다")
                .setBody("목표 거리 " + targetDistance + "km, 가족들과 함께 도전해보세요!")
                .build();

        // Android 설정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("challenge_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 챌린지 종료 알림 메시지 생성
     */
    private Message createChallengeCompletionNotificationMessage(int challengeId, String startDate,
                                                                 String endDate, int targetDistance,
                                                                 float totalDistance, int achievementRate,
                                                                 String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "CHALLENGE_COMPLETED");
        data.put("id", String.valueOf(challengeId));
        data.put("startDate", startDate);
        data.put("endDate", endDate);
        data.put("targetDistance", String.valueOf(targetDistance));
        data.put("totalDistance", String.format("%.1f", totalDistance));
        data.put("achievementRate", String.valueOf(achievementRate));
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        // 알림 메시지 제목과 내용 구성
        String title = "주간 산책 챌린지가 종료되었습니다";
        String body;

        if (achievementRate >= 100) {
            body = String.format("목표 %dkm 중 %.1fkm 달성! 목표를 완료했어요. 결과를 확인해보세요!",
                    targetDistance, totalDistance);
        } else {
            body = String.format("목표 %dkm 중 %.1fkm 달성! 달성률: %d%%. 다음 챌린지도 함께해요!",
                    targetDistance, totalDistance, achievementRate);
        }

        // 알림 정보
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // Android 설정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("challenge_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 유효하지 않은 토큰 에러인지 확인
     */
    private boolean isInvalidTokenError(FirebaseMessagingException e) {
        return e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
                || (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT
                && e.getMessage().contains("Invalid registration"));
    }

    /**
     * 채팅 메시지 알림 전송
     */
    public void sendChatMessageNotification(String senderName, String messageContent,
                                            String familyId, String messageId,
                                            String senderId, String messageType,
                                            Date timestamp, List<String> receiverIds) {
        log.info("채팅 메시지 알림 전송: familyId={}, messageId={}, receivers={}",
                familyId, messageId, receiverIds.size());

        if (receiverIds.isEmpty()) {
            log.debug("수신자가 없어 FCM 알림 전송 생략");
            return;
        }

        for (String receiverId : receiverIds) {
            try {
                int userIdInt = Integer.parseInt(receiverId);
                // 대상 사용자의 FCM 토큰 조회
                List<Fcm> targetTokens = fcmMapper.findByUserId(userIdInt);

                if (targetTokens.isEmpty()) {
                    log.warn("대상 사용자의 FCM 토큰이 없음: userId={}", receiverId);
                    continue;
                }

                for (Fcm fcm : targetTokens) {
                    try {
                        // 알림 메시지 생성
                        Message message = createChatMessageNotificationMessage(
                                senderName, messageContent, familyId, messageId,
                                senderId, messageType, timestamp, fcm.getFcmToken());

                        // 메시지 전송
                        String response = firebaseMessaging.send(message);
                        log.info("채팅 메시지 알림 전송 성공: userId={}, response={}", receiverId, response);
                    } catch (FirebaseMessagingException e) {
                        log.error("채팅 메시지 알림 전송 실패: token={}, error={}", fcm.getFcmToken(), e.getMessage());
                        // 유효하지 않은 토큰 처리
                        if (isInvalidTokenError(e)) {
                            fcmMapper.deleteFcmToken(fcm.getFcmId());
                            log.info("유효하지 않은 FCM 토큰 삭제: {}", fcm.getFcmToken());
                        }
                    }
                }
            } catch (NumberFormatException e) {
                log.error("사용자 ID 변환 실패: userId={}, error={}", receiverId, e.getMessage());
            }
        }
    }

    /**
     * 채팅 메시지 알림 메시지 생성
     */
    private Message createChatMessageNotificationMessage(String senderName, String messageContent,
                                                         String familyId, String messageId,
                                                         String senderId, String messageType,
                                                         Date timestamp, String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "CHAT");
        data.put("id", messageId);
        data.put("senderId", senderId);
        data.put("senderName", senderName);
        data.put("familyChatId", familyId);
        data.put("message", messageContent);
        data.put("messageType", messageType);
        data.put("timestamp", timestamp.toString());

        // 알림 정보
        Notification notification = Notification.builder()
                .setTitle(senderName)
                .setBody(messageContent)
                .build();

        // Android 설정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("chat_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 👟👟👟👟👟👟👟👟👟👟산책 시작 알림 전송👟👟👟👟👟👟👟👟👟👟
     */
    public void sendWalkStartNotification(UserDto walker, int familyId) {
        log.info("산책 시작 알림 전송: userId={}, familyId={}", walker.getUserId(), familyId);

        // 가족 구성원의 FCM 토큰 조회 (본인 제외)
        List<FcmTokenDTO> familyTokens = fcmMapper.findTokensByFamilyIdExceptUser(familyId, walker.getUserId());

        if (familyTokens.isEmpty()) {
            log.warn("알림을 받을 가족 구성원의 FCM 토큰이 없음: familyId={}", familyId);
            return;
        }

        for (FcmTokenDTO tokenDto : familyTokens) {
            try {
                // 알림 메시지 생성
                Message message = createWalkStartNotificationMessage(walker, tokenDto.getToken());

                // 메시지 전송
                String response = firebaseMessaging.send(message);
                log.info("산책 시작 알림 전송 성공: userId={}, response={}", tokenDto.getUserId(), response);
            } catch (FirebaseMessagingException e) {
                log.error("산책 시작 알림 전송 실패: userId={}, token={}, error={}",
                        tokenDto.getUserId(), tokenDto.getToken(), e.getMessage());
                if (isInvalidTokenError(e)) {
                    Fcm fcm = fcmMapper.findByUserIdAndFcmToken(tokenDto.getUserId(), tokenDto.getToken());
                    if (fcm != null) {
                        fcmMapper.deleteFcmToken(fcm.getFcmId());
                    }
                }
            }
        }
    }

    /**
     * 산책 종료 알림 전송
     */
    public void sendWalkEndNotification(UserDto walker, int familyId, float distance, int calories, long durationMinutes) {
        log.info("산책 종료 알림 전송: userId={}, familyId={}, distance={}, calories={}",
                walker.getUserId(), familyId, distance, calories);

        // 가족 구성원의 FCM 토큰 조회 (본인 제외)
        List<FcmTokenDTO> familyTokens = fcmMapper.findTokensByFamilyIdExceptUser(familyId, walker.getUserId());

        if (familyTokens.isEmpty()) {
            log.warn("알림을 받을 가족 구성원의 FCM 토큰이 없음: familyId={}", familyId);
            return;
        }

        for (FcmTokenDTO tokenDto : familyTokens) {
            try {
                // 알림 메시지 생성
                Message message = createWalkEndNotificationMessage(walker, distance, calories, durationMinutes, tokenDto.getToken());

                // 메시지 전송
                String response = firebaseMessaging.send(message);
                log.info("산책 종료 알림 전송 성공: userId={}, response={}", tokenDto.getUserId(), response);
            } catch (FirebaseMessagingException e) {
                log.error("산책 종료 알림 전송 실패: userId={}, token={}, error={}",
                        tokenDto.getUserId(), tokenDto.getToken(), e.getMessage());
                if (isInvalidTokenError(e)) {
                    Fcm fcm = fcmMapper.findByUserIdAndFcmToken(tokenDto.getUserId(), tokenDto.getToken());
                    if (fcm != null) {
                        fcmMapper.deleteFcmToken(fcm.getFcmId());
                    }
                }
            }
        }
    }

    /**
     * 산책 시작 알림 메시지 생성
     */
    private Message createWalkStartNotificationMessage(UserDto walker, String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "WALK_START");
        data.put("id", String.valueOf(walker.getUserId()));
        data.put("senderName", walker.getUserNickname() != null ? walker.getUserNickname() : "가족");


        String walkerName = walker.getUserNickname() != null ? walker.getUserNickname() : "가족";

        // 알림 정보
        Notification notification = Notification.builder()
                .setTitle("산책 알림")
                .setBody(walkerName + "님이 산책을 시작했어요! 함께 산책해보세요.")
                .build();

        // Android 설정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("walk_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 산책 종료 알림 메시지 생성
     */
    private Message createWalkEndNotificationMessage(UserDto walker, float distance, int calories, long durationMinutes, String token) {
        // Data 페이로드
        Map<String, String> data = new HashMap<>();
        data.put("type", "WALK_END");
        data.put("id", String.valueOf(walker.getUserId()));
        data.put("senderId", String.valueOf(walker.getUserId()));
        data.put("senderName", walker.getUserNickname() != null ? walker.getUserNickname() : "가족");
        data.put("distance", String.format("%.1f", distance));
        data.put("calories", String.valueOf(calories));
        data.put("duration", String.valueOf(durationMinutes));
        data.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));

        String walkerName = walker.getUserNickname() != null ? walker.getUserNickname() : "가족";
        String distanceStr = String.format("%.1f", distance);
        String durationStr = formatDuration(durationMinutes);

        // 알림 정보
        Notification notification = Notification.builder()
                .setTitle("산책 완료")
                .setBody(walkerName + "님이 " + distanceStr + "km 산책을 완료했어요! (" + durationStr + ", " + calories + "kcal)")
                .build();

        // Android 설정
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(AndroidNotification.builder()
                        .setChannelId("walk_channel")
                        .build())
                .build();

        return Message.builder()
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setAndroidConfig(androidConfig)
                .build();
    }

    /**
     * 시간(분) 포맷팅
     */
    private String formatDuration(long minutes) {
        if (minutes < 60) {
            return minutes + "분";
        } else {
            long hours = minutes / 60;
            long mins = minutes % 60;
            return hours + "시간 " + (mins > 0 ? mins + "분" : "");
        }
    }
}