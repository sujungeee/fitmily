package com.d208.fitmily.domain.health.dto;

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

    public String getHealthFiveMajorDiseases() {
        return fiveMajorDiseases;
    }

    public String getHealthOtherDiseases() {
        return otherDiseases;
    }

    public float getHealthHeight() {
        return height;
    }

    public float getHealthWeight() {
        return weight;
    }

    public float getHealthBmi() {
        return bmi;
    }
}
