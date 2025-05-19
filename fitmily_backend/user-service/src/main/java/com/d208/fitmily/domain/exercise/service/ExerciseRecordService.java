package com.d208.fitmily.domain.exercise.service;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordInsertDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordRequestDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordResponseDto;
import com.d208.fitmily.domain.exercise.mapper.ExerciseMapper;
import com.d208.fitmily.domain.walk.mapper.WalkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExerciseRecordService {
    private final ExerciseMapper exerciseMapper;
    private final WalkMapper walkMapper;

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

    // 운동 기록 + 달성률 업데이트
    @Transactional
    public void recordExercise(Integer userId, ExerciseRecordRequestDto dto){

        // 칼로리 계산
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

        // 달성률 업데이트
        try {
            Map<String, Object> progress = exerciseMapper.findGoalAndTodayTotal(userId, dto.getExerciseName());

            if (progress != null && progress.get("goalValue") != null) {
                int goalValue = ((Number) progress.get("goalValue")).intValue();
                int todayTotal = ((Number) progress.get("todayTotal")).intValue();
                int progressRate = Math.min(100, (int) Math.round((todayTotal / (double) goalValue) * 100));

                exerciseMapper.updateProgress(userId, dto.getExerciseName(), progressRate);
            }
        } catch (Exception e) {
            System.out.println("목표가 없어 progress 갱신 실패");
        }
    }

    // 운동 + 산책 기록 조회
    public List<ExerciseRecordResponseDto> getDailyExerciseRecords(Integer userId) {

        // 오늘 운동 기록
        List<ExerciseRecordResponseDto> exerciseRecords = exerciseMapper.findTodayExerciseRecords(userId);
        // 오늘 산책 기록
        List<ExerciseRecordResponseDto> walkRecords = walkMapper.findTodayWalkRecords(userId);

        // 둘 다 합쳐서 반환
        List<ExerciseRecordResponseDto> result = new ArrayList<>();
        result.addAll(walkRecords);
        result.addAll(exerciseRecords);

        return result;
    }
}
