package com.d208.fitmily.domain.exercise.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyGoalProgressDto {
    private String date;
    private int exerciseGoalProgress;
}
