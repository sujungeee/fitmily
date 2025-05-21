package com.d208.fitmily.domain.poke.service;

import com.d208.fitmily.domain.fcm.service.FcmService;
import com.d208.fitmily.domain.notification.entity.Notification;
import com.d208.fitmily.domain.notification.mapper.NotificationMapper;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.user.mapper.UserMapper;
import com.d208.fitmily.domain.user.service.UserService;
import com.d208.fitmily.domain.walk.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokeService {

        private final NotificationMapper notificationMapper;
        private final FcmService fcmService;
        private final UserService userService; // UserMapper 대신 UserService 사용

//    @Transactional
//    public LocalDateTime sendPoke(int senderId, int targetId) {
//        log.info("콕 찌르기 전송: senderId={}, targetId={}", senderId, targetId);
//
//        // 발신자 정보 조회 - UserDto 사용 (닉네임 포함)
//        UserDto sender = userService.getUserDtoById(senderId);
//        if (sender == null) {
//            log.error("발신자를 찾을 수 없음: userId={}", senderId);
//            throw new RuntimeException("존재하지 않는 사용자입니다.");
//        }
//
//        // 발신자 닉네임 확인 - UserDto의 getUserNickname() 메서드 사용
//        String senderNickname = sender.getUserNickname() != null ? sender.getUserNickname() : "사용자";
//        log.info("발신자 정보: userId={}, nickname={}", sender.getUserId(), senderNickname);
//
//        // 수신자 정보 조회
//        UserDto target = userService.getUserDtoById(targetId);
//        if (target == null) {
//            log.error("수신자를 찾을 수 없음: userId={}", targetId);
//            throw new RuntimeException("존재하지 않는 사용자입니다.");
//        }
//
//        // 현재 시간
//        LocalDateTime now = LocalDateTime.now();
//
//        // 알림 저장
//        Notification notification = new Notification();
//        notification.setUserId(targetId);
//        notification.setNotificationType("POKE");
//        notification.setNotificationSenderId(senderId);
//        notification.setNotificationReceiverId(targetId);
//        notification.setNotificationResourceId(0);
//        notification.setNotificationContent(senderNickname + "님이 당신에게 산책을 장려했어요!");
//        notification.setNotificationIsRead(0);
//        notification.setNotificationCreatedAt(now);
//        notification.setNotificationUpdatedAt(now);
//
//        notificationMapper.insertNotification(notification);
//        log.info("콕 찌르기 알림 저장 완료: notificationId={}", notification.getNotificationId());
//
//        // FCM 알림 전송 - User 대신 UserDto 사용
//        try {
//            // UserDto를 사용하므로 FcmService의 sendPokeNotification 메서드도 수정 필요
//            fcmService.sendPokeNotification(sender, target, notification.getNotificationId(), now);
//        } catch (Exception e) {
//            log.error("FCM 알림 전송 중 오류 발생: {}", e.getMessage());
//        }
//
//        return now;
//    }
//}

    public LocalDateTime sendPoke(int senderId, int targetId) {
        log.info("콕 찌르기 전송: senderId={}, targetId={}", senderId, targetId);

        // 발신자 정보 조회 - UserDto 사용 (닉네임 포함)
        UserDto sender = userService.getUserDtoById(senderId);
        if (sender == null) {
            log.error("발신자를 찾을 수 없음: userId={}", senderId);
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // 발신자 닉네임 확인
        String senderNickname = sender.getUserNickname() != null ? sender.getUserNickname() : "사용자";
        log.info("발신자 정보: userId={}, nickname={}", sender.getUserId(), senderNickname);

        // 수신자 정보 조회
        UserDto target = userService.getUserDtoById(targetId);
        if (target == null) {
            log.error("수신자를 찾을 수 없음: userId={}", targetId);
            throw new RuntimeException("존재하지 않는 사용자입니다.");
        }

        // 현재 시간
        LocalDateTime now = LocalDateTime.now();

        // FCM 알림 전송
        try {
            // 고유한 ID 생성 (현재 시간 밀리초)
            int pokeId = (int) (System.currentTimeMillis() % 100000);
            fcmService.sendPokeNotification(sender, target, pokeId, now);
        } catch (Exception e) {
            log.error("FCM 알림 전송 중 오류 발생: {}", e.getMessage());
        }

        return now;
    }
}