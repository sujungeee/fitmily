package com.d208.fitmily.domain.exercise.mapper;

import com.d208.fitmily.domain.exercise.entity.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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
}
