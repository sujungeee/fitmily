package com.d208.fitmily.domain.walk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EndWalkRequestDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Float distance;
    private String routeImg;

}