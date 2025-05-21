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
import java.time.format.DateTimeFormatter;
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
        try {
            List<NotificationResponseDTO> notifications = notificationMapper.findNotificationsByUserId(userId, size, page * size);

            for (NotificationResponseDTO notification : notifications) {
                String type = notification.getType();
                Map<String, Object> data = new HashMap<>();

                // resourceId 필드에서 값을 가져와 data 맵에 넣기
                try {
                    // 리플렉션 대신 직접 NotificationResponseDTO의 resourceId 필드에 접근
                    java.lang.reflect.Field field = notification.getClass().getDeclaredField("resourceId");
                    field.setAccessible(true);
                    Integer resourceId = (Integer) field.get(notification);

                    if ("POKE".equals(type)) {
                        data.put("pokeId", resourceId);
                    } else if ("CHALLENGE".equals(type)) {
                        data.put("challengeId", resourceId);
                        data.put("startDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
                    } else if ("WALK".equals(type)) {
                        data.put("walkId", resourceId);
                    }

                    notification.setData(data);
                } catch (Exception e) {
                    log.error("알림 데이터 처리 중 오류: {}", e.getMessage());
                }
            }

            return NotificationListResponseDTO.builder()
                    .content(notifications)
                    .build();
        } catch (Exception e) {
            log.error("알림 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("알림 목록 조회 중 오류가 발생했습니다.", e);
        }
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