package com.d208.fitmily.domain.health.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HealthResponseDto {
    private Integer healthId;
    private Float bmi;
    private Float height;
    private Float weight;

    @JsonIgnore
    private String otherDiseases;
    @JsonIgnore
    private String fiveMajorDiseases;


    // 응답용 리스트 필드
    private List<String> otherDiseasesList;
    private List<String> fiveMajorDiseasesList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
