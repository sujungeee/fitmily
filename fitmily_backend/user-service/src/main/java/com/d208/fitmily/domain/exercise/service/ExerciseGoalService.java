package com.d208.fitmily.domain.exercise.service;

import com.d208.fitmily.domain.exercise.dto.DailyGoalProgressDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalResponse;
import com.d208.fitmily.domain.exercise.dto.WeeklyGoalProgressResponse;
import com.d208.fitmily.domain.exercise.mapper.ExerciseGoalMapper;
import com.d208.fitmily.global.common.exception.BusinessException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ExerciseGoalService {

    private final ExerciseGoalMapper exerciseGoalMapper;

    /**
     * 운동 목표 목록 조회 및 진행률 업데이트
     * @param userId 사용자 ID
     * @return 운동 목표 응답 객체
     */
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

            dto.setGoalId(((Number) map.get("exercise_goal_id")).intValue());
            dto.setExerciseGoalName((String) map.get("exercise_goal_name"));

            // Number 타입으로 직접 받아서 float으로 변환
            dto.setExerciseGoalValue(((Number) map.get("exercise_goal_value")).floatValue());
            dto.setExerciseRecordValue(((Number) map.get("exercise_record_value")).floatValue());

            goals.add(dto);

            // 개별 목표 진행률 업데이트
            try {
                int goalProgress = calculateIndividualProgress(
                        dto.getExerciseRecordValue(),
                        dto.getExerciseGoalValue()
                );

                exerciseGoalMapper.updateGoalProgress(
                        userId,
                        dto.getExerciseGoalName(),
                        goalProgress
                );
            } catch (Exception e) {
            }
        }

        // 응답 생성
        ExerciseGoalResponse response = new ExerciseGoalResponse();
        response.setExerciseGoalProgress(progress);
        response.setGoal(goals);

        return response;
    }


    /**
     * 개별 목표 진행률 계산
     * @param currentValue 현재 기록 값
     * @param targetValue 목표 값
     * @return 진행률 (0-100%)
     */
    private int calculateIndividualProgress(float currentValue, float targetValue) {
        if (targetValue <= 0) return 0;  // 목표값이 0이하면 진행률 0

        // 현재값이 목표값 이상이면 100% 달성
        if (currentValue >= targetValue) return 100;

        // 그렇지 않으면 진행률 계산
        int progress = (int) Math.round((currentValue / targetValue) * 100);
        return progress;
    }


    /**
     * 운동 목표 등록 (중복 체크)
     * @param userId 사용자 ID
     * @param data 운동 목표 정보
     * @throws BusinessException 이미 존재하는 운동 목표일 경우
     */
    public void createGoal(Integer userId, ExerciseGoalDto data) {
        // 중복 체크
        int count = exerciseGoalMapper.countGoalByNameAndUserId(userId, data.getExerciseGoalName());
        if (count > 0) {
            throw new BusinessException(ErrorCode.EXERCISE_GOAL_ALREADY_EXISTS);
        }
        try {
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
    public void updateGoal(Integer userId, Integer goalId, float exerciseValue) {
        try {
            int updated = exerciseGoalMapper.updateGoal(userId, goalId, exerciseValue, new Date());
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


    public WeeklyGoalProgressResponse getWeeklyGoalProgress(int userId) {
        // 오늘 날짜
        LocalDate today = LocalDate.now();

        // 7일 전 날짜
        LocalDate sevenDaysAgo = today.minusDays(6);

        // log.info("조회 기간: {} ~ {}", sevenDaysAgo, today);

        List<DailyGoalProgressDto> dailyProgressList = new ArrayList<>();

        // 7일간의 데이터 조회
        for (LocalDate date = sevenDaysAgo; !date.isAfter(today); date = date.plusDays(1)) {
            String dateStr = date.toString();

            // 해당 날짜의 목표 달성률 계산
            int totalGoals = exerciseGoalMapper.countGoalsByDateAndUser(userId, dateStr);
            int completedGoals = exerciseGoalMapper.countCompletedGoalsByDateAndUser(userId, dateStr);

            // 목표 달성률 계산 (목표가 없으면 0%)
            int progressRate = totalGoals > 0 ? (int)Math.round((double)completedGoals / totalGoals * 100) : 0;

            // 결과 목록에 추가
            dailyProgressList.add(DailyGoalProgressDto.builder()
                    .date(dateStr)
                    .exerciseGoalProgress(progressRate)
                    .build());
        }

        return WeeklyGoalProgressResponse.builder()
                .goal(dailyProgressList)
                .build();
    }

}