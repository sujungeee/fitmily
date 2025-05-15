package com.d208.fitmily.domain.family.mapper;

import com.d208.fitmily.domain.family.entity.Family;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FamilyMapper {

    /**
     * 가족 생성
     */
    void createFamily(Family family);

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
