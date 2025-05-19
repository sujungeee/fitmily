package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Integer userId;
    private String nickname;
    private Integer familySequence;
    private String zodiacName;
    private Float distanceCompleted;
    private Integer rank;
}