package com.d208.fitmily.health.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HealthResponseDto {
    private Integer healthId;
    private Float bmi;
    private Float height;
    private Float weight;
    private String otherDiseases;
    private String fiveMajorDiseases;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
