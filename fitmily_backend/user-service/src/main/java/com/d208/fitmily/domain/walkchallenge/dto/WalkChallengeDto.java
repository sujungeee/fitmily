package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkChallengeDto {
    private Integer challengeId;
    private Integer familyId;
    private Integer targetDistance;
    private LocalDate startDate;
}