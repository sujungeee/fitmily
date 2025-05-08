package com.d208.fitmily.walk.entity;

import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Walk {
    private Integer walkId;            // PK
    private Integer userId;            // FK → user.user_id
    private String  walkRouteImg;
    private LocalDateTime walkStartTime;
    private LocalDateTime walkEndTime;
    private Float   walkDistance;
    private Integer walkHeartRate;
    private Integer walkCalories;
    private Float   walkAvgSpeed;
    private Integer walkStepCount;

    // createdAt/updatedAt 는 DB default CURRENT_TIMESTAMP 로 자동 관리하므로
    // 여긴 필드만 두고, insert 문에 안넣어줘도댐
    private LocalDateTime walkCreatedAt;
    private LocalDateTime walkUpdatedAt;

}
