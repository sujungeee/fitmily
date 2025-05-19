package com.d208.fitmily.domain.walk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// gps로 저장할 데이터

public class GpsDto {
    private Integer userId;
    private Double lat;
    private Double lon;
    private Long timestamp;
}
