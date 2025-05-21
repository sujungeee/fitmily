package com.d208.fitmily.domain.poke.service;

import com.d208.fitmily.domain.fcm.service.FcmService;
import com.d208.fitmily.domain.notification.entity.Notification;
import com.d208.fitmily.domain.notification.mapper.NotificationMapper;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PokeService {

    private final FcmService fcmService;
    private final UserMapper userMapper;
    private final NotificationMapper notificationMapper;

    /**
     * 콕 찌르기 전송
     */
    @Transactional
    public LocalDateTime sendPoke(int senderId, int targetUserId) {
        log.info("콕 찌르기 전송: senderId={}, targetId={}", senderId, targetUserId);

// 사용자 확인
        User sender = userMapper.selectById(senderId);
        if (sender == null) {
            log.error("발신자를 찾을 수 없습니다: userId={}", senderId);
            throw new RuntimeException("발신자를 찾을 수 없습니다: " + senderId);
        }

// 디버깅 로그 추가
        log.info("발신자 정보: userId={}, nickname={}", sender.getUserId(), sender.getUserNickname());

        User target = userMapper.selectById(targetUserId);
        if (target == null) {
            log.error("수신자를 찾을 수 없습니다: userId={}", targetUserId);
            throw new RuntimeException("수신자를 찾을 수 없습니다: " + targetUserId);
        }

// 현재 시간
        LocalDateTime now = LocalDateTime.now();

// notification 테이블에 저장
        Notification notification = new Notification();
        notification.setUserId(targetUserId);
        notification.setNotificationType("POKE");
        notification.setNotificationSenderId(senderId);
        notification.setNotificationReceiverId(targetUserId);

// 닉네임 null 체크 추가
        String senderNickname = sender.getUserNickname();
        if (senderNickname == null) {
            senderNickname = "사용자";
            log.warn("발신자의 닉네임이 null입니다: userId={}", sender.getUserId());
        }
        notification.setNotificationContent(senderNickname + "님이 당신에게 산책을 장려했어요!");
        notification.setNotificationIsRead(0);
        notification.setNotificationCreatedAt(now);
        notification.setNotificationUpdatedAt(now);

        notificationMapper.insertNotification(notification);
        log.info("콕 찌르기 알림 저장 완료: notificationId={}", notification.getNotificationId());

        // FCM 알림 전송 - 이제 notification ID를 resource ID로 사용
//        fcmService.sendPokeNotification(sender, target, notification.getNotificationId(), now);

        return now;
    }
}