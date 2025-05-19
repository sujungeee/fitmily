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
            #{userId}, #{routeImg}, #{startTime}, #{endTime}, #{distance}, #{calories},NOW(),NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "walkId")
    int insertStopWalk(StopWalkDto walk);

    // 2. 산책 기록 조회
    @Select("""
        <script>
        SELECT
            w.walk_id, w.user_id, u.user_nickname,u.user_zodiac_name,
            w.walk_route_img, w.walk_start_time, w.walk_end_time,
            w.walk_distance, w.walk_calories
        FROM walk w
        LEFT JOIN user u ON u.user_id = w.user_id
        <where>
            <if test="userId != null">
                AND w.user_id = #{userId}
            </if>
            <if test="start != null">
                AND w.walk_start_time <![CDATA[ >= ]]> #{start}
            </if>
            <if test="end != null">
                AND w.walk_end_time <![CDATA[ <= ]]> #{end}
            </if>
        </where>
        ORDER BY w.walk_start_time DESC
        </script>
        """)
    @Results(id = "WalkResponseDtoMap", value = {
            @Result(column = "walk_id", property = "walkId"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "walk_route_img", property = "routeImg"),
            @Result(column = "walk_start_time", property = "startTime"),
            @Result(column = "walk_end_time", property = "endTime"),
            @Result(column = "walk_distance", property = "distance"),
            @Result(column = "walk_calories", property = "calories"),
            @Result(column = "user_zodiac_name", property = "zodiacName"),
            @Result(column = "user_nickname", property = "nickname")
    })
    List<WalkResponseDto> selectWalks(Map<String, Object> params);

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
