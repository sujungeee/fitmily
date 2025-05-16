package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWalkChallengeDto {
    private Integer userId;
    private Integer challengeId;
    private Float distance;
}