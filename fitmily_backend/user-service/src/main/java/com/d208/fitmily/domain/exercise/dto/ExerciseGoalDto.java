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
    private String exercise_goal_name;    // 운동 이름
    private String exercise_goal_value;   // 목표 값
    private String exercise_record_value; // 현재 기록 값
}