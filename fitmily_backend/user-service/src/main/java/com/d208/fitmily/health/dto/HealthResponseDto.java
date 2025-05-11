package com.d208.fitmily.health.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class HealthResponseDto {
    private Integer healthId;
    private Float bmi;
    private Float height;
    private Float weight;
    private String otherDiseases;
    private String fiveMajorDiseases;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
}
