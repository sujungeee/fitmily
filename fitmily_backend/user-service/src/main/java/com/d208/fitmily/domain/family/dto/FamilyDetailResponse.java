package com.d208.fitmily.domain.family.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FamilyDetailResponse {
    private FamilyData data;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FamilyData {
        private String familyName;
        private String familyInviteCode;
    }
}