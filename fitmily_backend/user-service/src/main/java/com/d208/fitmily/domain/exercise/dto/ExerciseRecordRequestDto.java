package com.d208.fitmily.domain.exercise.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExerciseRecordRequestDto {
    private String exerciseName;
    private Integer exerciseCount;
    private Integer exerciseTime;
}