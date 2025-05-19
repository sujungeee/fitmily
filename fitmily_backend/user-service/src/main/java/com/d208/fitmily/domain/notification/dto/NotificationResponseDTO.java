package com.d208.fitmily.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationResponseDTO {
    private int notificationId;
    private String type;
    private LocalDateTime receivedAt;
    private boolean isRead;
    private Integer senderId;
    private String senderNickname;
    private Integer resourceId;
    private Map<String, Object> data;
}

