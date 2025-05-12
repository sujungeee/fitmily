package com.d208.fitmily.health.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHealthResponseDto {
    private Integer userId;
    private Float bmi;
    private Float height;
    private Float weight;
    private String otherDiseases;
    private String fiveMajorDiseases;
}
