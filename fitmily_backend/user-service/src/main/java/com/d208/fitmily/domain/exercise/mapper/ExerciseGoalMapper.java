package com.d208.fitmily.domain.exercise.mapper;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ExerciseGoalMapper {

    // 1. 사용자의 전체 운동 목표 진행률 계산
    @Select("""
        SELECT COALESCE(ROUND(AVG(
            CASE
                WHEN eg.exercise_goal_name = '산책' THEN
                    CASE
                        WHEN w_distance.total_distance IS NULL THEN 0
                        WHEN w_distance.total_distance >= CAST(eg.exercise_goal_value AS DECIMAL(10,1)) THEN 100
                        ELSE (w_distance.total_distance / CAST(eg.exercise_goal_value AS DECIMAL(10,1))) * 100
                    END
                ELSE
                    CASE
                        WHEN e_counts.count_sum IS NULL THEN 0
                        WHEN e_counts.count_sum >= CAST(eg.exercise_goal_value AS DECIMAL(10,1)) THEN 100
                        ELSE (e_counts.count_sum / CAST(eg.exercise_goal_value AS DECIMAL(10,1))) * 100
                    END
            END
        )), 0) as progress
        FROM exercise_goal eg
        LEFT JOIN (
            SELECT e.exercise_name, SUM(e.exercise_count) AS count_sum
            FROM exercise e
            WHERE e.user_id = #{userId} AND DATE(e.exercise_created_at) = CURDATE()
            GROUP BY e.exercise_name
        ) e_counts ON eg.exercise_goal_name = e_counts.exercise_name
        LEFT JOIN (
            SELECT SUM(w.walk_distance) AS total_distance
            FROM walk w
            WHERE w.user_id = #{userId} AND DATE(w.walk_created_at) = CURDATE()
        ) w_distance ON eg.exercise_goal_name = '산책'
        WHERE eg.user_id = #{userId}
        """)
    int calculateProgress(@Param("userId") Integer userId);

    // 2. 사용자의 운동 목표 목록 조회
    @Select("""
    SELECT
        eg.exercise_goal_id,
        eg.exercise_goal_name,
        eg.exercise_goal_value,  -- CAST 제거
        CASE
            WHEN eg.exercise_goal_name = '산책' THEN
                COALESCE(
                    (SELECT SUM(w.walk_distance)
                     FROM walk w
                     WHERE w.user_id = #{userId} AND DATE(w.walk_created_at) = CURDATE()),
                    0
                )
            ELSE
                COALESCE(
                    (SELECT SUM(e.exercise_count)
                     FROM exercise e
                     WHERE e.user_id = #{userId}
                       AND e.exercise_name = eg.exercise_goal_name
                       AND DATE(e.exercise_created_at) = CURDATE()),
                    0
                )
        END AS exercise_record_value  -- CAST 제거
    FROM exercise_goal eg
    WHERE eg.user_id = #{userId}
    """)
    List<Map<String, Object>> selectGoalsByUserId(@Param("userId") Integer userId);

    // 3. 운동 목표 등록
    @Insert("""
        INSERT INTO exercise_goal (
            user_id,
            exercise_goal_name,
            exercise_goal_value,
            exercise_goal_progress,
            exercise_goal_created_at,
            exercise_goal_updated_at
        ) VALUES (
            #{userId},
            #{goal.exerciseGoalName},
            #{goal.exerciseGoalValue},
            0,
            #{now},
            #{now}
        )
        """)
    int insertGoal(
            @Param("userId") Integer userId,
            @Param("goal") ExerciseGoalDto goal,
            @Param("now") Date now
    );

    // 4. 운동 목표 수정
    @Update("""
        UPDATE exercise_goal
        SET exercise_goal_value = #{exerciseValue},
            exercise_goal_updated_at = #{now}
        WHERE exercise_goal_id = #{goalId}
          AND user_id = #{userId}
        """)
    int updateGoal(
            @Param("userId") Integer userId,
            @Param("goalId") Integer goalId,
            @Param("exerciseValue") float exerciseValue,
            @Param("now") Date now
    );

    // 5. 운동 목표 삭제
    @Delete("""
        DELETE FROM exercise_goal
        WHERE exercise_goal_id = #{goalId}
          AND user_id = #{userId}
        """)
    int deleteGoal(
            @Param("userId") Integer userId,
            @Param("goalId") Integer goalId
    );

    // 6. 운동 목표 진행률 업데이트
    @Update("""
        UPDATE exercise_goal
        SET exercise_goal_progress = #{progress},
            exercise_goal_updated_at = NOW()
        WHERE user_id = #{userId}
          AND exercise_goal_name = #{exerciseName}
        """)
    int updateGoalProgress(
            @Param("userId") Integer userId,
            @Param("exerciseName") String exerciseName,
            @Param("progress") Integer progress
    );

    // 7. 중복 운동 목표 확인
    @Select("""
        SELECT COUNT(*)
        FROM exercise_goal
        WHERE user_id = #{userId}
          AND exercise_goal_name = #{exerciseName}
        """)
    int countGoalByNameAndUserId(
            @Param("userId") Integer userId,
            @Param("exerciseName") String exerciseName
    );
}