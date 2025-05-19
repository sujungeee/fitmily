package com.d208.fitmily.domain.family.dto;

import lombok.*;

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
        private List<GoalInfo> goals;  // exercises에서 goals로 변경
        private int totalProgressRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GoalInfo {  // ExerciseInfo에서 GoalInfo로 변경
        private int exerciseGoalId;  // exerciseId에서 변경
        private String exerciseGoalName;  // exerciseName에서 변경
        private int exerciseGoalValue;  // 새로운 필드
        private int exerciseGoalProgress;  // 새로운 필드
    }
}
