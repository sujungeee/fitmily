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
    private String exerciseGoalName;  // 운동 이름
    private float exerciseGoalValue; // 목표값
}