package com.d208.fitmily.domain.family.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDailyExerciseResponse {
    private List<MemberDailyExercise> members;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberDailyExercise {
        private int userId;
        private String userNickname;
        private String userZodiacName; // request from 현슴
        private int userFamilySequence;
        private int exerciseGoalProgress;
        private int totalCalories;
        private int totalTime;
        private List<ExerciseInfo> exercises;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseInfo {
        private int exerciseId;
        private String exerciseName;
        private String exerciseRouteImg;
        private float exerciseCount;
        private int exerciseGoalValue;
        private int exerciseCalories;
        private int exerciseTime;
    }
}
