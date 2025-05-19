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
    private String routeImg;
    private Timestamp startTime;
    private Timestamp endTime;
    private Float distance;
    private Integer calories;
}
