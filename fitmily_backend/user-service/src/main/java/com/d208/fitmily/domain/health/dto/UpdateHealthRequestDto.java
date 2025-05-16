package com.d208.fitmily.domain.health.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateHealthRequestDto {
    private Float height;
    private Float weight;
    private List<String> otherDiseases;
    private List<String> fiveMajorDiseases;
}