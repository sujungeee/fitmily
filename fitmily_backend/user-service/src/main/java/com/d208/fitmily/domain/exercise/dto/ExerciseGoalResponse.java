package com.d208.fitmily.domain.exercise.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ExerciseGoalResponse {
    private int exercise_goal_progress;
    private List<ExerciseGoalDto> goal;
}