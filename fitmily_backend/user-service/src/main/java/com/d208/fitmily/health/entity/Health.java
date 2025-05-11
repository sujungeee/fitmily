package com.d208.fitmily.health.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class Health {
    private Integer healthId;
    private Integer userId;
    private Float   height;
    private Float   weight;
    private Float   bmi;
    private String  otherDiseases;
    private String  fiveMajorDiseases;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
