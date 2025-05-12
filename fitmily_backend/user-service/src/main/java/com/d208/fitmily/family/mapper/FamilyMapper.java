package com.d208.fitmily.family.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Arrays;

@Mapper
public interface FamilyMapper {
    // 더미 구현
    default boolean checkFamilyMembership(@Param("familyId") String familyId, @Param("userId") String userId) {
        // 항상 true 반환
        return true;
    }

    default List<String> selectFamilyMemberIds(@Param("familyId") String familyId) {
        // 더미 데이터 반환
        return Arrays.asList("1", "2", "3");
    }

    default int countFamilyMembers(@Param("familyId") String familyId) {
        // 더미 값 반환
        return 3;
    }
}