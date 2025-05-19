package com.d208.fitmily.domain.family.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyDetailResponse {
    // data 래퍼 없이 직접 필드 노출
    private String familyName;
    private String familyInviteCode;
    private int familyPeople;
}
