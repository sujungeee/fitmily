package com.d208.fitmily.domain.exercise.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
@Data
public class ExerciseRecordRequestDto {
    private String exerciseName;
    private Integer exerciseCount;
    private Integer exerciseTime;
}