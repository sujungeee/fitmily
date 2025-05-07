package com.d208.fitmily.health.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HealthResponseDto {
    private Integer healthId;
    private Float healthBmi;
    private Float healthHeight;
    private Float healthWeight;
    private Float healthBodyFatPercentage;
    private String healthDisease;
    private LocalDateTime healthCreatedAt;
    private LocalDateTime healthUpdatedAt;
}
