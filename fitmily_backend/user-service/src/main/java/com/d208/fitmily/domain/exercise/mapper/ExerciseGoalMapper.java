package com.d208.fitmily.domain.exercise.mapper;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExerciseGoalMapper {
    /**
     * 사용자의 전체 운동 목표 진행률 계산
     * @param userId 사용자 ID
     * @return 전체 진행률 (%)
     */
    int calculateProgress(@Param("userId") Integer userId);

    /**
     * 사용자의 운동 목표 목록 조회
     * @param userId 사용자 ID
     * @return 운동 목표 맵 리스트
     */
    List<Map<String, Object>> selectGoalsByUserId(@Param("userId") Integer userId);

    /**
     * 운동 목표 등록
     * @param userId 사용자 ID
     * @param goal 운동 목표 정보
     * @param now 현재 시간
     * @return 영향 받은 행 수
     */
    int insertGoal(
            @Param("userId") Integer userId,
            @Param("goal") ExerciseGoalDto goal,
            @Param("now") Date now
    );

    /**
     * 운동 목표 수정
     * @param userId 사용자 ID
     * @param goalId 목표 ID
     * @param exerciseValue 수정할 목표값
     * @param now 현재 시간
     * @return 영향 받은 행 수
     */
    int updateGoal(
            @Param("userId") Integer userId,
            @Param("goalId") Integer goalId,
            @Param("exerciseValue") String exerciseValue,
            @Param("now") Date now
    );

    /**
     * 운동 목표 삭제
     * @param userId 사용자 ID
     * @param goalId 목표 ID
     * @return 영향 받은 행 수
     */
    int deleteGoal(
            @Param("userId") Integer userId,
            @Param("goalId") Integer goalId
    );

    /**
     * 운동 목표 진행률 업데이트
     * @param userId 사용자 ID
     * @param exerciseName 운동 이름
     * @param progress 업데이트할 진행률
     * @return 영향 받은 행 수
     */
    int updateGoalProgress(
            @Param("userId") Integer userId,
            @Param("exerciseName") String exerciseName,
            @Param("progress") Integer progress
    );

    /**
     * 특정 사용자의 특정 운동 목표 존재 여부 확인
     * @param userId 사용자 ID
     * @param exerciseName 운동 이름
     * @return 존재하는 목표 수 (0 또는 1)
     */
    int countGoalByNameAndUserId(
            @Param("userId") Integer userId,
            @Param("exerciseName") String exerciseName
    );
}