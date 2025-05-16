package com.d208.fitmily.domain.health.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHealthResponseDto {
    private Integer userId;
    private Float bmi;
    private Float height;
    private Float weight;
    private String otherDiseases;
    private String fiveMajorDiseases;
}
