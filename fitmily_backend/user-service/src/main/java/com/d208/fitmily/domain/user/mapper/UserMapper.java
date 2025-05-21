package com.d208.fitmily.domain.user.mapper;

import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.walk.dto.UserDto;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    // 1) 아이디 중복 조회
    @Select("SELECT EXISTS(SELECT 1 FROM user WHERE user_login_id = #{loginId})")
    boolean existsByLoginId(@Param("loginId") String loginId);

    // 2) 로그인 아이디로 User 조회
    @Select("""
        SELECT
            user_id              AS userId,
            user_login_id        AS loginId,
            user_pw              AS password,
            user_nickname        AS nickname,
            user_birth           AS birth,
            user_gender          AS gender,
            user_refresh_token   AS refreshToken,
            user_role            AS role,
            user_zodiac_name     AS zodiacName,
            family_id       AS familyId
        FROM user
        WHERE user_login_id = #{loginId}
        """)
    User findByLoginId(@Param("loginId") String loginId);

    // 3) 회원 가입
    @Insert("""
        INSERT INTO user (
            user_login_id, user_pw, user_nickname,
            user_birth, user_gender, user_refresh_token,
            user_zodiac_name, user_family_sequence,
            user_created_at, user_updated_at
        ) VALUES (
            #{loginId}, #{password}, #{nickname},
            #{birth}, #{gender}, #{refreshToken},
            #{zodiacName}, #{familySequence},
            NOW(), NOW()
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);

    // 4) 리프레시 토큰 업데이트
    @Update("UPDATE user SET user_refresh_token = #{refreshToken} WHERE user_id = #{userId}")
    int updateRefreshToken(@Param("userId") Integer userId, @Param("refreshToken") String refreshToken);

    // 5) 리프레시 토큰 제거 (로그아웃)
    @Update("UPDATE user SET user_refresh_token = NULL WHERE user_id = #{userId}")
    int clearRefreshToken(@Param("userId") Integer userId);

    // 6) 아이디 + 토큰 일치 여부 확인
    @Select("SELECT EXISTS(SELECT 1 FROM user WHERE user_id = #{userId} AND user_refresh_token = #{refreshToken})")
    boolean existsByIdAndRefreshToken(@Param("userId") Integer userId, @Param("refreshToken") String refreshToken);

    // 7) userId로 조회
    @Select("SELECT user_id AS userId, user_login_id AS loginId, user_birth AS birth, user_gender AS gender FROM user WHERE user_id = #{userId}")
    User selectById(@Param("userId") Integer userId);

    // 8) 산책할 때 user 정보 조회
    @Select("""
        SELECT
            user_id AS userId,
            user_family_sequence AS userFamilySequence,
            user_zodiac_name AS userZodiacName,
            user_nickname AS userNickname,
            family_id AS familyId
        FROM user
        WHERE user_id = #{userId}
        """)
    UserDto getUserDtoById(@Param("userId") Integer userId);

    // 9) 패밀리 id로 userId 조회
    @Select("SELECT user_id FROM user WHERE family_id = #{familyId}")
    List<Integer> getUserIdsByFamilyId(@Param("familyId") Integer familyId);

    @Select("""
        <script>
        SELECT user_id, user_nickname, user_zodiac_name, user_family_sequence,family_id
        FROM user
        WHERE user_id IN
        <foreach collection='userIds' item='id' open='(' separator=',' close=')'>
            #{id}
        </foreach>
        </script>
    """)
    @Results({
            @Result(column = "user_id", property = "userId"),
            @Result(column = "user_nickname", property = "nickname"),
            @Result(column = "user_zodiac_name", property = "zodiacName"),
            @Result(column = "user_family_sequence", property = "familySequence"),
            @Result(column = "family_id", property = "familyId")

    })
    List<User> getUsersByIds(@Param("userIds") List<Integer> userIds);


    @Select("SELECT user_id FROM user WHERE family_id = #{familyId}")
    List<Integer> selectUserIdsByFamilyId(@Param("familyId") Integer familyId);
}
