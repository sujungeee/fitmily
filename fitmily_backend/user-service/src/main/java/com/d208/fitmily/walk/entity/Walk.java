package com.d208.fitmily.walk.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Walk {
    private Integer walkId;            // PK
    private Integer exerciseGoalId;    // FK → exercise_goal.exercise_goal_id
    private Integer userId;            // FK → user.user_id
    private String  walkRouteImg;
    private LocalDateTime walkStartTime;
    private LocalDateTime walkEndTime;
    private Float   walkDistance;
    private Integer walkHeartRate;
    private Integer walkCalories;
    private Float   walkAvgSpeed;

    // createdAt/updatedAt 는 DB default CURRENT_TIMESTAMP 로 자동 관리하므로
    // 여긴 필드만 두고, insert 문에 안넣어줘도댐
    private LocalDateTime walkCreatedAt;
    private LocalDateTime walkUpdatedAt;

}
