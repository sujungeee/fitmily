package com.d208.fitmily.domain.exercise.entity;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class Exercise {
    private int exerciseId;      // 운동 고유번호
    private int userId;          // 사용자 ID
    private String exerciseName; // 운동 이름
    private Integer exerciseTime;  // 운동 시간 (NULL 가능)
    private int exerciseCount;   // 운동 횟수
    private int exerciseCalories; // 소모 칼로리
    private LocalDateTime exerciseCreatedAt; // 생성 시간
    private LocalDateTime exerciseUpdatedAt; // 수정 시간
}
