package com.d208.fitmily.domain.notification.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Notification {
    private int notificationId;
    private int userId;
    private String notificationType;
    private int notificationSenderId;
    private int notificationReceiverId;
    private Integer notificationResourceId;
    private String notificationContent;
    private int notificationIsRead;
    private LocalDateTime notificationCreatedAt;
    private LocalDateTime notificationUpdatedAt;
}