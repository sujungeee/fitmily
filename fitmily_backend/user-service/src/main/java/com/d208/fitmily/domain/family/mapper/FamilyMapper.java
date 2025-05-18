package com.d208.fitmily.domain.family.mapper;

import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.user.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FamilyMapper {

    /**
     * 가족 생성
     */
    @Insert("INSERT INTO family (family_name, family_invite_code, family_people, family_created_at, family_updated_at) " +
            "VALUES (#{familyName}, #{familyInviteCode}, #{familyPeople}, #{familyCreatedAt}, #{familyUpdatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "familyId")
    void createFamily(Family family);

    /**
     * 초대 코드로 패밀리 조회
     */
    @Select("SELECT * FROM family WHERE family_invite_code = #{inviteCode}")
    @Results(id = "familyMap", value = {
            @Result(property = "familyId", column = "family_id"),
            @Result(property = "familyName", column = "family_name"),
            @Result(property = "familyInviteCode", column = "family_invite_code"),
            @Result(property = "familyPeople", column = "family_people"),
            @Result(property = "familyCreatedAt", column = "family_created_at"),
            @Result(property = "familyUpdatedAt", column = "family_updated_at")
    })
    Family findByInviteCode(@Param("inviteCode") String inviteCode);

    /**
     * 패밀리 인원 수 증가
     */
    @Update("UPDATE family SET family_people = family_people + 1, family_updated_at = NOW() WHERE family_id = #{familyId}")
    void incrementFamilyPeople(@Param("familyId") int familyId);

    /**
     * 사용자의 패밀리 ID 업데이트
     */
    @Update("UPDATE user SET family_id = #{familyId} WHERE user_id = #{userId}")
    void updateUserFamilyId(@Param("userId") int userId, @Param("familyId") int familyId);

    /**
     * 패밀리 ID로 패밀리 조회
     */
    @Select("SELECT * FROM family WHERE family_id = #{familyId}")
    @ResultMap("familyMap")  // 이미 정의된 매핑 재사용
    Family findById(@Param("familyId") int familyId);

    /**
     * 패밀리 구성원 목록 조회
     */
    @Select("SELECT * FROM user WHERE family_id = #{familyId}")
    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "user_id"),
            @Result(property = "loginId", column = "login_id"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "birth", column = "birth"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "familyId", column = "family_id"),
            @Result(property = "role", column = "role"),
            @Result(property = "zodiacName", column = "zodiac_name"),
            @Result(property = "familySequence", column = "user_family_sequence"), // userFamilySequence → familySequence
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "updatedAt", column = "updated_at")
    })
    List<User> findFamilyMembers(@Param("familyId") int familyId);



    //
    /**
     * 가족 구성원 확인
     */
    @Select("SELECT COUNT(*) FROM user WHERE family_id = #{familyId} AND user_id = #{userId}")
    boolean checkFamilyMembership(@Param("familyId") String familyId, @Param("userId") String userId);

    /**
     * 가족 구성원 수 조회
     */
    @Select("SELECT COUNT(*) FROM user WHERE family_id = #{familyId}")
    int countFamilyMembers(@Param("familyId") String familyId);

    /**
     * 가족 구성원 ID 목록 조회
     */
    @Select("SELECT user_id FROM user WHERE family_id = #{familyId}")
    List<String> selectFamilyMemberIds(@Param("familyId") String familyId);

}

///**
// * 가족 구성원 확인
// */
//@Select("SELECT COUNT(*) FROM user WHERE family_id = #{familyId} AND user_id = #{userId}")
//boolean checkFamilyMembership(@Param("familyId") String familyId, @Param("userId") String userId);
//
///**
// * 가족 구성원 수 조회
// */
//@Select("SELECT COUNT(*) FROM user WHERE family_id = #{familyId}")
//int countFamilyMembers(@Param("familyId") String familyId);
//
///**
// * 가족 구성원 ID 목록 조회
// */
//@Select("SELECT user_id FROM user WHERE family_id = #{familyId}")
//List<String> selectFamilyMemberIds(@Param("familyId") String familyId);