package com.d208.fitmily.domain.exercise.mapper;

import com.d208.fitmily.domain.exercise.dto.ExerciseRecordInsertDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordResponseDto;
import com.d208.fitmily.domain.exercise.entity.Exercise;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExerciseMapper {
    //    운동 기록 추가
    @Insert("""
        INSERT INTO exercise(
            user_id,
            exercise_name,
            exercise_count,
            exercise_time,
            exercise_calories,
            exercise_created_at,
            exercise_updated_at
        ) VALUES (
            #{userId},
            #{exerciseName},
            #{exerciseCount},
            #{exerciseTime},
            #{exerciseCalories},
              NOW(),
              NOW()
        )
    """)
    void insertExerciseRecord(ExerciseRecordInsertDto dto);

    // 당일한 운동 총합, 목표 조회
    @Select("""
    SELECT g.exercise_goal_value AS goalValue,
           COALESCE(SUM(r.exercise_count), 0) AS todayTotal
    FROM exercise_goal g
    LEFT JOIN exercise r
         ON g.user_id = r.user_id
         AND g.exercise_goal_name = r.exercise_name
         AND DATE(r.exercise_created_at) = CURDATE()
    WHERE g.user_id = #{userId} AND g.exercise_goal_name = #{exerciseName}
    GROUP BY g.exercise_goal_value
    """)

    Map<String, Object> findGoalAndTodayTotal(@Param("userId") Integer userId, @Param("exerciseName") String exerciseName);


    // 달성률 업데이트
    @Update("""
    UPDATE exercise_goal
    SET exercise_goal_progress = #{progressRate}
    WHERE user_id = #{userId} AND exercise_goal_name = #{exerciseName}
    """)
    void updateProgress(@Param("userId") Integer userId,
                        @Param("exerciseName") String exerciseName,
                        @Param("progressRate") Integer progressRate);

    // 운동 기록 조회
    @Select("""
        SELECT
            NULL AS walkId,
            NULL AS imgUrl,
            e.exercise_calories AS exerciseCalories,
            e.exercise_count AS exerciseRecord,
            e.exercise_name AS exerciseName
        FROM exercise e
        WHERE e.user_id = #{userId} AND DATE(e.exercise_created_at) = CURDATE()
    """)
    List<ExerciseRecordResponseDto> findTodayExerciseRecords(Integer userId);


    //

    /**
     * 특정 사용자의 특정 날짜 총 운동 칼로리 계산
     */
    @Select("SELECT COALESCE(SUM(exercise_calories), 0) FROM exercise WHERE user_id = #{userId} AND DATE_FORMAT(exercise_created_at, '%Y-%m-%d') = #{date}")
    int calculateUserTotalCalories(@Param("userId") int userId, @Param("date") String date);


    /**
     * 특정 사용자의 특정 날짜 운동 목록 조회
     */
    @Select("SELECT * FROM exercise WHERE user_id = #{userId} AND DATE_FORMAT(exercise_created_at, '%Y-%m-%d') = #{date}")
    @Results(id = "exerciseMap", value = {
            @Result(property = "exerciseId", column = "exercise_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "exerciseName", column = "exercise_name"),
            @Result(property = "exerciseTime", column = "exercise_time"),
            @Result(property = "exerciseCount", column = "exercise_count"),
            @Result(property = "exerciseCalories", column = "exercise_calories"),
            @Result(property = "exerciseCreatedAt", column = "exercise_created_at"),
            @Result(property = "exerciseUpdatedAt", column = "exercise_updated_at")
    })
    List<Exercise> findUserExercisesByDate(@Param("userId") int userId, @Param("date") String date);


    /**
     * 운동 ID로 경로 이미지 조회
     */
    @Select("SELECT route_image FROM walk WHERE exercise_id = #{exerciseId}")
    @Results(id = "walkRouteMap", value = {
            @Result(property = "routeImage", column = "route_image")
    })
    String findRouteImageByExerciseId(@Param("exerciseId") int exerciseId);


}
