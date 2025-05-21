package com.d208.fitmily.domain.fcm.mapper;

import com.d208.fitmily.domain.fcm.dto.FcmTokenDTO;
import com.d208.fitmily.domain.fcm.entity.Fcm;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FcmMapper {

    // FCM 토큰 등록
    @Insert("""
        INSERT INTO fcm (
            user_id, fcm_token, fcm_created_at, fcm_updated_at
        ) VALUES (
            #{userId}, #{fcmToken}, #{fcmCreatedAt}, #{fcmUpdatedAt}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "fcmId")
    int insertFcmToken(Fcm fcm);

    // FCM 토큰 업데이트
    @Update("""
        UPDATE fcm
        SET fcm_updated_at = #{fcmUpdatedAt}
        WHERE user_id = #{userId} AND fcm_token = #{fcmToken}
        """)
    int updateFcmToken(Fcm fcm);

    // 사용자 ID와 토큰으로 FCM 토큰 조회
    @Select("""
        SELECT 
            fcm_id as fcmId,
            user_id as userId,
            fcm_token as fcmToken,
            fcm_created_at as fcmCreatedAt,
            fcm_updated_at as fcmUpdatedAt
        FROM fcm
        WHERE user_id = #{userId} AND fcm_token = #{fcmToken}
        """)
    Fcm findByUserIdAndFcmToken(@Param("userId") int userId, @Param("fcmToken") String fcmToken);

    // 사용자 ID로 FCM 토큰 목록 조회
    @Select("""
        SELECT 
            fcm_id as fcmId,
            user_id as userId,
            fcm_token as fcmToken,
            fcm_created_at as fcmCreatedAt,
            fcm_updated_at as fcmUpdatedAt
        FROM fcm
        WHERE user_id = #{userId}
        """)
    List<Fcm> findByUserId(@Param("userId") int userId);

    // 유효하지 않은 FCM 토큰 삭제
    @Delete("""
        DELETE FROM fcm
        WHERE fcm_id = #{fcmId}
        """)
    int deleteFcmToken(@Param("fcmId") int fcmId);

    // 가족 구성원들의 FCM 토큰 목록 조회
    @Select("""
        SELECT f.user_id as userId, f.fcm_token as token
        FROM fcm f
        JOIN user u ON f.user_id = u.user_id
        WHERE u.family_id = #{familyId}
        """)
    List<FcmTokenDTO> findTokensByFamilyId(@Param("familyId") int familyId);

    // 가족 구성원들의 FCM 토큰 목록 조회 (특정 사용자 제외 - 산책)
    @Select("""
        SELECT f.user_id as userId, f.fcm_token as token
        FROM fcm f
        JOIN user u ON f.user_id = u.user_id
        WHERE u.family_id = #{familyId} AND u.user_id != #{excludeUserId}
        """)
    List<FcmTokenDTO> findTokensByFamilyIdExceptUser(@Param("familyId") int familyId, @Param("excludeUserId") int excludeUserId);
}