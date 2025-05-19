package com.d208.fitmily.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnreadNotificationDTO {
    private boolean hasUnreadNotifications;
    private int unreadCount;
}

