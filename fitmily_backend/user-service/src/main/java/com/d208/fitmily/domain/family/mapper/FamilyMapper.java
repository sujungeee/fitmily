package com.d208.fitmily.domain.family.mapper;

import com.d208.fitmily.domain.family.entity.Family;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FamilyMapper {

    /**
     * 가족 생성
     */
    void createFamily(Family family);

    /**
     * 초대 코드로 패밀리 조회
     */
    @Select("SELECT * FROM family WHERE family_invite_code = #{inviteCode}")
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
    Family findById(@Param("familyId") int familyId);
    

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
