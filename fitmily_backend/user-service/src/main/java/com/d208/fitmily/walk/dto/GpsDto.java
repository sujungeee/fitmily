package com.d208.fitmily.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// gps로 저장할 데이터
public class GpsDto {
    private Double lat;
    private Double lon;
    private Long timestamp;
}
