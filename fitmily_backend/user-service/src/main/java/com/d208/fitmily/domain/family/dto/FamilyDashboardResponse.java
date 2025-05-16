package com.d208.fitmily.domain.family.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDashboardResponse {
    private String date;
    private List<FamilyMember> members;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FamilyMember {
        private int userId;
        private String userNickname;
        private String userZodiacName;
        private int userFamilySequence;
        private List<ExerciseInfo> exercises;  // goals → exercises
        private int totalProgressRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseInfo {
        private int exerciseId;
        private String exerciseName;
        private Integer exerciseTime;  // 운동 시간
        private int exerciseCount;     // 운동 횟수
        private int exerciseCalories;  // 소모 칼로리
    }
}
