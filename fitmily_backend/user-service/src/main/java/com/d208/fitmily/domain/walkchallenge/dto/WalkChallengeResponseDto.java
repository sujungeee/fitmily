package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalkChallengeResponseDto {
    private Integer challengeId;
    private LocalDate startDate;
    private Integer targetDistance;
    private Integer progressPercentage;
    private List<ParticipantDto> participants;
}