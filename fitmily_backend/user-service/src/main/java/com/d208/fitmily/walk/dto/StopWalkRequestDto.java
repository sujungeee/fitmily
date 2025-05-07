package com.d208.fitmily.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StopWalkRequestDto {
    private LocalDateTime walkStartTime;
    private LocalDateTime walkEndTime;
    private Float walkDistance;
    private Integer walkHeartRate;
    private String walkRouteImg;
    private Integer stepCount;
}