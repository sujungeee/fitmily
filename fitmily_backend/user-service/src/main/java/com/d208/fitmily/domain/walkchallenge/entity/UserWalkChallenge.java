package com.d208.fitmily.domain.walkchallenge.entity;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserWalkChallenge {
    private Integer userWalkChallengeId;
    private Integer userId;
    private Integer challengeId;
    private Float distance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}