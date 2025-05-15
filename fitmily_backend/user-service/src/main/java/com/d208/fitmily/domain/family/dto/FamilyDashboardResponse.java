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
        private List<ExerciseGoalInfo> goals;
        private int totalProgressRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ExerciseGoalInfo {
        private int exerciseGoalId;
        private String exerciseGoalName;
        private int exerciseGoalValue;
        private int exerciseGoalProgress;
    }
}