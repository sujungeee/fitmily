package com.d208.fitmily.domain.walkchallenge.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Integer userId;
    private String nickname;
    private String profileColor;
    private Float distanceCompleted;
    private Integer rank;
}