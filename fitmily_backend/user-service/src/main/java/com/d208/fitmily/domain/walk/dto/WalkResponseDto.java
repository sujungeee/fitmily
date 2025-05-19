package com.d208.fitmily.domain.walk.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalkResponseDto {
    private Integer walkId;
    private Integer userId;
    private String  RouteImg;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float distance;
    private Integer calories;
    private String nickname;
    private String zodiacName;

}
