package com.d208.fitmily.domain.exercise.mapper;

import com.d208.fitmily.domain.exercise.dto.ExerciseRecordInsertDto;
import com.d208.fitmily.domain.exercise.entity.Exercise;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExerciseMapper {
    /**
     * 특정 사용자의 특정 날짜 운동 목록 조회
     */
    @Select("SELECT * FROM exercise WHERE user_id = #{userId} AND DATE(exercise_created_at) = #{date}")
    List<Exercise> findUserExercisesByDate(@Param("userId") int userId, @Param("date") String date);

    /**
     * 특정 사용자의 특정 날짜 총 운동 칼로리 계산
     */
    @Select("SELECT COALESCE(SUM(exercise_calories), 0) FROM exercise WHERE user_id = #{userId} AND DATE(exercise_created_at) = #{date}")
    int calculateUserTotalCalories(@Param("userId") int userId, @Param("date") String date);


    //    운동 기록 추가
    @Insert("""
        INSERT INTO exercise_record (
            user_id,
            exercise_name,
            exercise_record,
            exercise_time,
            exercise_calories
        ) VALUES (
            #{userId},
            #{exerciseName},
            #{exerciseRecord},
            #{exerciseTime},
            #{exerciseCalories}
        )
    """)
    void insertExerciseRecord(ExerciseRecordInsertDto dto);

    @Select("""
    SELECT g.goal_value AS goalValue,
           COALESCE(SUM(r.exercise_record), 0) AS todayTotal
    FROM exercise_goal g LEFT JOIN exercise_record r ON g.user_id = r.user_id
                               AND g.exercise_goal_name = r.exercise_name
                               AND DATE(r.created_at) = CURDATE()
    WHERE g.user_id = #{userId}
      AND g.exercise_goal_name = #{exerciseName}
    GROUP BY g.goal_value
    """)
    Map<String, Integer> findGoalAndTodayTotal(@Param("userId") Integer userId, @Param("exerciseName") String exerciseName);


    @Update("""
    UPDATE exercise_goal
    SET progress_value = #{progressValue},
        progress_rate = #{progressRate}
    WHERE user_id = #{userId}
      AND exercise_goal_name = #{exerciseName}
    """)
    void updateProgress(@Param("userId") Integer userId, @Param("exerciseName") String exerciseName, @Param("progressValue") Integer progressValue, @Param("progressRate") Integer progressRate);






}
