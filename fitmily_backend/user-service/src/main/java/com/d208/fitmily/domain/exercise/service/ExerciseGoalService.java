package com.d208.fitmily.domain.exercise.service;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalResponse;
import com.d208.fitmily.domain.exercise.mapper.ExerciseGoalMapper;
import com.d208.fitmily.global.common.exception.BusinessException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExerciseGoalService {

    private final ExerciseGoalMapper exerciseGoalMapper;

    /**
     * 운동 목표 목록 조회 및 진행률 업데이트
     * @param userId 사용자 ID
     * @return 운동 목표 응답 객체
     */
    public ExerciseGoalResponse getGoals(Integer userId) {
        // 전체 진행률 계산
        int progress = exerciseGoalMapper.calculateProgress(userId);

        // 목표 목록 조회 (Map으로 받음)
        List<Map<String, Object>> goalMaps = exerciseGoalMapper.selectGoalsByUserId(userId);

        // DTO로 변환
        List<ExerciseGoalDto> goals = new ArrayList<>();
        for (Map<String, Object> map : goalMaps) {
            ExerciseGoalDto dto = new ExerciseGoalDto();
            dto.setExercise_goal_name((String) map.get("exercise_goal_name"));
            dto.setExercise_goal_value((String) map.get("exercise_goal_value"));
            dto.setExercise_record_value((String) map.get("exercise_record_value"));
            goals.add(dto);

            // 개별 목표 진행률 업데이트 (DB에 저장)
            try {
                // 문자열을 직접 전달하여 진행률 계산
                int goalProgress = calculateIndividualProgress(
                        dto.getExercise_record_value(),
                        dto.getExercise_goal_value()
                );

                exerciseGoalMapper.updateGoalProgress(
                        userId,
                        dto.getExercise_goal_name(),
                        goalProgress
                );
            } catch (Exception e) {
                // 예외 발생 시 로깅만 하고 계속 진행
            }
        }

        // 응답 생성
        ExerciseGoalResponse response = new ExerciseGoalResponse();
        response.setExercise_goal_progress(progress);
        response.setGoal(goals);

        return response;
    }

    /**
     * 개별 목표 진행률 계산
     * @param currentValueStr 현재 기록 값 (문자열)
     * @param targetValueStr 목표 값 (문자열)
     * @return 진행률 (0-100%)
     */
    private int calculateIndividualProgress(String currentValueStr, String targetValueStr) {
        try {
            double currentValue = Double.parseDouble(currentValueStr);
            double targetValue = Double.parseDouble(targetValueStr);

            if (targetValue <= 0) return 0;  // 목표값이 0이하면 진행률 0

            // 현재값이 목표값 이상이면 100% 달성
            if (currentValue >= targetValue) return 100;

            // 그렇지 않으면 진행률 계산
            int progress = (int) Math.round((currentValue / targetValue) * 100);
            return progress;
        } catch (NumberFormatException e) {
            return 0;  // 변환 실패시 0% 반환
        }
    }

    /**
     * 운동 목표 등록 (중복 체크 포함)
     * @param userId 사용자 ID
     * @param data 운동 목표 정보
     * @throws BusinessException 이미 존재하는 운동 목표일 경우
     */
    public void createGoal(Integer userId, ExerciseGoalDto data) {
        // 중복 체크
        int count = exerciseGoalMapper.countGoalByNameAndUserId(userId, data.getExercise_goal_name());
        if (count > 0) {
            throw new BusinessException(ErrorCode.EXERCISE_GOAL_ALREADY_EXISTS);
        }

        try {
            String formattedValue = formatDecimalValue(data.getExercise_goal_value());
            data.setExercise_goal_value(formattedValue);

            int inserted = exerciseGoalMapper.insertGoal(userId, data, new Date());
            if (inserted == 0) {
                throw new BusinessException(ErrorCode.EXERCISE_GOAL_CREATE_FAILED);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXERCISE_GOAL_CREATE_FAILED);
        }
    }

    /**
     * 운동 목표 수정
     * @param userId 사용자 ID
     * @param goalId 목표 ID
     * @param exerciseValue 수정할 목표값
     */
    public void updateGoal(Integer userId, Integer goalId, String exerciseValue) {
        try {
            // 소수점 1자리까지 변환 처리 (필요시)
            String formattedValue = formatDecimalValue(exerciseValue);

            int updated = exerciseGoalMapper.updateGoal(userId, goalId, formattedValue, new Date());
            if (updated == 0) {
                throw new BusinessException(ErrorCode.EXERCISE_GOAL_NOT_FOUND);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXERCISE_GOAL_UPDATE_FAILED);
        }
    }

    /**
     * 소수점 1자리까지 변환
     * @param value 변환할 값
     * @return 소수점 1자리까지 포맷팅된 값
     */
    private String formatDecimalValue(String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            return String.format("%.1f", doubleValue);
        } catch (NumberFormatException e) {
            return value;
        }
    }
    /**
     * 운동 목표 삭제
     * @param userId 사용자 ID
     * @param goalId 목표 ID
     */
    public void deleteGoal(Integer userId, Integer goalId) {
        try {
            int deleted = exerciseGoalMapper.deleteGoal(userId, goalId);
            if (deleted == 0) {
                throw new BusinessException(ErrorCode.EXERCISE_GOAL_NOT_FOUND);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.EXERCISE_GOAL_DELETE_FAILED);
        }
    }
}