package com.d208.fitmily.domain.walk.mapper;

import com.d208.fitmily.domain.exercise.dto.ExerciseRecordResponseDto;
import com.d208.fitmily.domain.walk.dto.StopWalkDto;
import com.d208.fitmily.domain.walk.dto.WalkResponseDto;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface WalkMapper {

    // 1. 산책 중지 시 데이터 저장
    @Insert("""
        INSERT INTO walk (
            user_id, walk_route_img, walk_start_time, walk_end_time, walk_distance, walk_calories,walk_created_at,walk_updated_at
        ) VALUES (
             #{userId}, #{walkRouteImg}, #{walkStartTime}, #{walkEndTime}, #{walkDistance}, #{calories}, NOW(), NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "walkId")
    int insertStopWalk(StopWalkDto walk);

    @Select("""
        SELECT
            w.walk_id,
            w.user_id,
            w.walk_route_img,
            w.walk_start_time,
            w.walk_end_time,
            w.walk_distance,
            w.walk_calories,
            u.nickname,
            u.zodiac_name,
            u.user_family_sequence
        FROM walk w
        JOIN user u ON w.user_id = u.user_id
        WHERE w.user_id = #{userId}
    """)
    @Results({
            @Result(property = "walkId", column = "walk_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "routeImg", column = "walk_route_img"),
            @Result(property = "startTime", column = "walk_start_time"),
            @Result(property = "endTime", column = "walk_end_time"),
            @Result(property = "distance", column = "walk_distance"),
            @Result(property = "calories", column = "walk_calories"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "zodiacName", column = "zodiac_name"),
            @Result(property = "userFamilySequence", column = "user_family_sequence")
    })
    List<WalkResponseDto> selectWalks(@Param("userId") Integer userId);

    // 3. 산책 목표 존재 여부 확인
    @Select("""
        SELECT EXISTS (
            SELECT 1 FROM exercise_goal
            WHERE user_id = #{userId}
              AND exercise_goal_name = 'walk'
              AND DATE(exercise_goal_created_at) = CURDATE()
        )
        """)
    Boolean walkGoalExists(@Param("userId") Integer userId);


    @Select("""
    SELECT
        w.walk_id AS walkId,
        w.walk_route_img AS imgUrl,
        w.walk_calories AS exerciseCalories,
        w.walk_distance AS exerciseRecord,
        '산책' AS exerciseName
    FROM walk w
    WHERE w.user_id = #{userId}
      AND DATE(w.walk_created_at) = CURDATE()
""")
    List<ExerciseRecordResponseDto> findTodayWalkRecords(Integer userId);
}
