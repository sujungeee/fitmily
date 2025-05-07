package com.d208.fitmily.health.entity;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Health {
    private Integer healthId;           // AUTO_INCREMENT PK
    private Integer userId;             // FK → user.user_id
    private Float   healthBmi;
    private Float   healthHeight;
    private Float   healthWeight;
    private Float   healthBodyFatPercentage;
    private String  healthDisease;
    private LocalDateTime healthCreatedAt;
    private LocalDateTime healthUpdatedAt;

}
