package com.d208.fitmily.domain.notification.service;

import com.d208.fitmily.domain.notification.dto.NotificationListResponseDTO;
import com.d208.fitmily.domain.notification.dto.NotificationResponseDTO;
import com.d208.fitmily.domain.notification.dto.UnreadNotificationDTO;
import com.d208.fitmily.domain.notification.dto.UnreadNotificationResponseDTO;
import com.d208.fitmily.domain.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationMapper notificationMapper;

    /**
     * 사용자별 알림 목록 조회
     */
    @Transactional(readOnly = true)
    public NotificationListResponseDTO getNotifications(int userId, int page, int size) {
        List<NotificationResponseDTO> notifications = notificationMapper.findNotificationsByUserId(userId, size, page * size);

        for (NotificationResponseDTO notification : notifications) {
            String type = notification.getType();
            Integer resourceId = notification.getResourceId();

            Map<String, Object> data = new HashMap<>();
            if ("POKE".equals(type)) {
                data.put("pokeId", resourceId);
            } else if ("CHALLENGE".equals(type)) {
                data.put("challengeId", resourceId);
                data.put("startDate", LocalDateTime.now().toLocalDate().toString());
            } else if ("WALK".equals(type)) {
                data.put("walkId", resourceId);
            }

            notification.setData(data);
        }

        return NotificationListResponseDTO.builder()
                .content(notifications)
                .build();
    }

    /**
     * 읽지 않은 알림 정보 조회
     */
    @Transactional(readOnly = true)
    public UnreadNotificationResponseDTO getUnreadNotifications(int userId) {
        int unreadCount = notificationMapper.countUnreadNotifications(userId);

        UnreadNotificationDTO data = UnreadNotificationDTO.builder()
                .hasUnreadNotifications(unreadCount > 0)
                .unreadCount(unreadCount)
                .build();

        return UnreadNotificationResponseDTO.builder()
                .data(data)
                .build();
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(int notificationId) {
        notificationMapper.markAsRead(notificationId, LocalDateTime.now());
    }
}