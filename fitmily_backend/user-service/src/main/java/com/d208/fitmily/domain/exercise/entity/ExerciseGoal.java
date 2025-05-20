package com.d208.fitmily.domain.exercise.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ExerciseGoal {
    private int exerciseGoalId;       // 운동 목표 고유번호
    private int userId;               // 사용자 ID
    private String exerciseGoalName;  // 운동 목표 이름
    private float exerciseGoalValue;  // 목표값 (횟수 또는 거리)
    private int exerciseGoalProgress; // 진행률 (0-100%)
    private LocalDateTime exerciseGoalCreatedAt; // 생성 시간
    private LocalDateTime exerciseGoalUpdatedAt; // 수정 시간
}
