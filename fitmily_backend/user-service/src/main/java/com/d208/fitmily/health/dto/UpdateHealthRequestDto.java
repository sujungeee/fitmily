package com.d208.fitmily.health.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateHealthRequestDto {
    private Float height;
    private Float weight;
    private String otherDiseases;
    private String fiveMajorDiseases;
}