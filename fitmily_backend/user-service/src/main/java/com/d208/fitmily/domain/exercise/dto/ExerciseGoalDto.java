package com.d208.fitmily.domain.exercise.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 개별 운동 목표 정보를 담는 DTO 클래스
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ExerciseGoalDto {
    private int goalId;
    private String exerciseGoalName;    // 운동 이름
    private float exerciseGoalValue;   // 목표 값
    private float exerciseRecordValue; // 현재 기록 값
}