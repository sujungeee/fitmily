package com.d208.fitmily.domain.exercise.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExerciseRecordResponseDto {
    private String imgUrl;
    private Integer walkId;                // 산책일 때만 존재
    private Integer exerciseCalories;      // 칼로리 소모량
    private Float exerciseRecord;          // 운동량 (거리 또는 횟수 등)
}