package com.d208.fitmily.domain.fcm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Fcm {
    private int fcmId;
    private int userId;
    private String fcmToken;
    private LocalDateTime fcmCreatedAt;
    private LocalDateTime fcmUpdatedAt;
}