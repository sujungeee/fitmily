package com.d208.fitmily.domain.walkchallenge.mapper;

import com.d208.fitmily.domain.walkchallenge.dto.UserWalkChallengeDto;
import com.d208.fitmily.domain.walkchallenge.dto.WalkChallengeDto;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface WalkChallengeMapper {

    // 새 챌린지 생성
    @Insert("""
        INSERT INTO walk_challenge (
            family_id, walk_challenge_target_distance, walk_challenge_start_date
        ) VALUES (
            #{familyId}, #{targetDistance}, #{startDate}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "challengeId")
    int insertWalkChallenge(WalkChallengeDto walkChallenge);

    // 사용자 챌린지 참여 등록
    @Insert("""
        INSERT INTO user_walk_challenge (
            user_id, challenge_id, user_walk_challenge_distance
        ) VALUES (
            #{userId}, #{challengeId}, #{distance}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userWalkChallengeId")
    int insertUserWalkChallenge(UserWalkChallengeDto userWalkChallenge);

    // 산책 기록 추가 시 챌린지 거리 업데이트
    @Update("""
        UPDATE user_walk_challenge
        SET 
            user_walk_challenge_distance = user_walk_challenge_distance + #{distance},
            user_walk_challenge_updated_at = NOW()
        WHERE 
            user_id = #{userId} 
            AND challenge_id = #{challengeId}
        """)
    int updateUserWalkChallengeDistance(UserWalkChallengeDto userWalkChallenge);

    // 특정 가족의 현재 활성 챌린지 조회
    @Select("""
        SELECT 
            challenge_id as challengeId,
            family_id as familyId,
            walk_challenge_target_distance as targetDistance,
            walk_challenge_start_date as startDate
        FROM walk_challenge
        WHERE 
            family_id = #{familyId} 
            AND walk_challenge_start_date <= CURDATE()
        ORDER BY walk_challenge_start_date DESC
        LIMIT 1
        """)
    WalkChallengeDto findActiveChallenge(@Param("familyId") Integer familyId);

    // 특정 가족의 이전 챌린지 조회 (가장 최근 것)
    @Select("""
        SELECT 
            challenge_id as challengeId,
            family_id as familyId,
            walk_challenge_target_distance as targetDistance,
            walk_challenge_start_date as startDate
        FROM walk_challenge
        WHERE 
            family_id = #{familyId} 
            AND walk_challenge_start_date < #{currentStartDate}
        ORDER BY walk_challenge_start_date DESC
        LIMIT 1
        """)
    WalkChallengeDto findPreviousChallenge(
            @Param("familyId") Integer familyId,
            @Param("currentStartDate") LocalDate currentStartDate
    );

    // 특정 챌린지의 총 달성 거리 조회
    @Select("""
        SELECT SUM(user_walk_challenge_distance)
        FROM user_walk_challenge
        WHERE challenge_id = #{challengeId}
        """)
    Float getTotalDistanceByChallenge(@Param("challengeId") Integer challengeId);

    // 현재 진행 중인 챌린지의 참가자 목록과 랭킹 조회
    @Select("""
    SELECT 
        uwc.user_id,
        u.user_nickname as nickname,
        '#FF5733' as profileColor,
        uwc.user_walk_challenge_distance as distanceCompleted,
        DENSE_RANK() OVER (ORDER BY uwc.user_walk_challenge_distance DESC) as user_rank
    FROM user_walk_challenge uwc
    JOIN user u ON u.user_id = uwc.user_id
    WHERE uwc.challenge_id = #{challengeId}
    ORDER BY user_rank, u.user_nickname
    """)
    List<Map<String, Object>> getChallengeParticipantsWithRank(@Param("challengeId") Integer challengeId);

    // 모든 가족 ID 조회
    @Select("""
        SELECT family_id
        FROM family
        """)
    List<Integer> getAllFamilyIds();

    @Update("""
    UPDATE user_walk_challenge uwc
    SET user_walk_challenge_distance = (
        SELECT COALESCE(SUM(walk_distance), 0)
        FROM walk w
        WHERE w.user_id = uwc.user_id
        AND w.walk_start_time >= (
            SELECT wc.walk_challenge_start_date
            FROM walk_challenge wc
            WHERE wc.challenge_id = uwc.challenge_id
        )
        AND w.walk_end_time <= NOW()
    ),
    user_walk_challenge_updated_at = NOW()
    WHERE uwc.challenge_id = #{challengeId}
    """)
    void syncUserWalkChallengeDistances(@Param("challengeId") Integer challengeId);
}