package com.d208.fitmily.walk.entity;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Walk {
    private Integer walkId;
    private Integer userId;
    private String  routeImg;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float distance;
    private Integer calories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
