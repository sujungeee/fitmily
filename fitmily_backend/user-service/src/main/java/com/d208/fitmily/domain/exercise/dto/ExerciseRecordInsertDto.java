package com.d208.fitmily.domain.exercise.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ExerciseRecordInsertDto {
    private Integer userId;
    private String exerciseName;
    private Integer exerciseCount;
    private Integer exerciseTime;
    private Integer exerciseCalories;

}
