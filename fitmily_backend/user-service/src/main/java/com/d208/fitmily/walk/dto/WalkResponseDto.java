package com.d208.fitmily.walk.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder             // ← 이걸 추가해야 WalkResponseDto.builder() 가 생성됩니다.
public class WalkResponseDto {
    private Integer walkId;
    private Integer userId;
    private String  walkRouteImg;
    private LocalDateTime walkStartTime;
    private LocalDateTime walkEndTime;
    private Float   walkDistance;
    private Integer walkHeartRate;
    private Integer walkCalories;

    private String userNickname;
    private String userProfileImg;

    //유저이미지
    //유저 닉네임
}
