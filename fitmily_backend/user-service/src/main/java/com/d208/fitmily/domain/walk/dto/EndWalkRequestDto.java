package com.d208.fitmily.domain.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EndWalkRequestDto {
    private Timestamp walkStartTime;
    private Timestamp walkEndTime;
    private Float walkDistance;
    private String walkRouteImg;
}