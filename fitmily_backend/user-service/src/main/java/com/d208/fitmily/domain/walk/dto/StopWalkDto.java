package com.d208.fitmily.domain.walk.dto;

import lombok.*;

import java.sql.Timestamp;

@Data
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StopWalkDto {
    private Integer walkId;
    private Integer userId;
    private String walkRouteImg;
    private Timestamp walkStartTime;
    private Timestamp walkEndTime;
    private Float walkDistance;
    private Integer calories;
}
