package com.d208.fitmily.domain.health.dto;

import lombok.Data;

@Data
public class HealthInsertDto {
    private Integer userId;
    private Float height;
    private Float weight;
    private Float bmi;

    // JSON 문자열로 직렬화한 결과를 담을 필드
    private String healthOtherDiseasesJson;
    private String healthFiveMajorDiseasesJson;
}