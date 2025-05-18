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
public class FamilyHealthStatusResponse {
    private List<MemberHealthInfo> members;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MemberHealthInfo {
        private int userId;
        private String userNickname;
        private String userBirth;
        private int userGender;
        private float healthHeight;
        private float healthWeight;
        private float healthBmi;
        private List<String> healthFiveMajorDiseases;
        private List<String> healthOtherDiseases;
    }
}
