package com.d208.fitmily.domain.exercise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 운동 목표 수정 API 요청 클래스
 */
@Getter
@Setter
@NoArgsConstructor
public class ExerciseGoalUpdateRequest {
    private float exerciseGoalValue; // 수정할 목표값
}