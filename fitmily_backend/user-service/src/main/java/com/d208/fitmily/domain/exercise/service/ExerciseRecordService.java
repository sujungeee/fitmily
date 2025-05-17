package com.d208.fitmily.domain.exercise.service;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordInsertDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordRequestDto;
import com.d208.fitmily.domain.exercise.mapper.ExerciseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {
    private final ExerciseMapper exerciseMapper;

    private static final Map<String, Float> CALORIES = Map.of(
            "벤치프레스", 0.5f,
            "푸쉬업", 0.3f,
            "스쿼트", 0.4f,
            "버피테스트", 1.0f,
            "데드리프트", 0.6f,
            "풀업", 1.0f,
            "딥스", 0.5f,
            "사이드레터럴레이즈", 0.3f
    );

    public void recordExercise(Integer userId, ExerciseRecordRequestDto dto){

        //칼로리 계산 해서 기록 추가
        String exerciseName = dto.getExerciseName();
        int count = dto.getExerciseCount();

        float oneCalories = CALORIES.get(exerciseName);
        int totalCalories = Math.round(oneCalories * count);

        ExerciseRecordInsertDto record = new ExerciseRecordInsertDto();
        record.setUserId(userId);
        record.setExerciseName(dto.getExerciseName());
        record.setExerciseTime(dto.getExerciseTime());
        record.setExerciseCount(dto.getExerciseCount());
        record.setExerciseCalories(totalCalories);

        exerciseMapper.insertExerciseRecord(record);

        //목표 + 오늘 총합 운동량 조회
        Map<String, Object> progress = exerciseMapper.findGoalAndTodayTotal(
                userId, dto.getExerciseName()
        );

        // sql 연산한 값을 JDBC 드라이버가 알아서 BigDecimal로 변환해버림 (Number)로 받음
        int goalValue = ((Number) progress.get("goalValue")).intValue();  // 목표
        int todayTotal = ((Number) progress.get("todayTotal")).intValue(); // 운동 총합

        //달성률 계산
        int progressRate = (int) Math.round((todayTotal / (double) goalValue) * 100);

        //목표 테이블 업데이트
        exerciseMapper.updateProgress(userId, exerciseName, progressRate);


    }

}
