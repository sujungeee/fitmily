package com.d208.fitmily.walk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GpsDto {
    private Integer userId;
    private Double latitude;
    private Double longitude;
    private Long timestamp;
}
