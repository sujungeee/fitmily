package com.d208.fitmily.domain.health.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddHealthRequestDto {

    @Schema(hidden = true)
    private Integer userId;

    @Schema(hidden = true)
    private Integer healthId;

    private Float height;
    private Float weight;

    @Schema(hidden = true)
    private Integer bmi;

    private List<String> OtherDiseases;
    private List<String> FiveMajorDiseases;

}
