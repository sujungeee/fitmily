package com.d208.fitmily.fcm.dto;
// 알림 DTO

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FCMNotificationDTO {
    private String title;
    private String body;
    private String imageUrl;
    private Object data;
}