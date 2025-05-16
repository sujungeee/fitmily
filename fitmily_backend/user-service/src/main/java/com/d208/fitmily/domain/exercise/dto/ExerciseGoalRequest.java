package com.d208.fitmily.domain.exercise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 목표 등록 API 요청 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class ExerciseGoalRequest {
    private String exercise_goal_name;  // 운동 이름
    private String exercise_goal_value; // 목표값
}