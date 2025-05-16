package com.d208.fitmily.domain.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StopWalkDto {
    private Integer userId;
    private String routeImg;
    private Timestamp startTime;
    private Timestamp endTime;
    private Float distance;
    private Float calories;
}
