package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserWalkChallengeDto {
    private Integer userWalkChallengeId; // 이 필드 추가
    private Integer userId;
    private Integer challengeId;
    private Float distance;
}