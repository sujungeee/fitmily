package com.d208.fitmily.domain.walkchallenge.entity;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkChallenge {
    private Integer challengeId;
    private Integer familyId;
    private Integer targetDistance;
    private LocalDate startDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}