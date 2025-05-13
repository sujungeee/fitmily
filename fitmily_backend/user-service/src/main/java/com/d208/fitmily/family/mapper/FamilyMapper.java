package com.d208.fitmily.family.mapper;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 가족 관련 더미 매퍼 (실제 MyBatis 또는 JPA 구현으로 대체 예정)
 */
@Component
public class FamilyMapper {

    /**
     * 가족 구성원 확인
     * @param familyId 가족 ID
     * @param userId 사용자 ID
     * @return 소속 여부
     */
    public boolean checkFamilyMembership(String familyId, String userId) {
        // 더미 구현 - 테스트 용도로 항상 true 반환
        return true;
    }

    /**
     * 가족 구성원 수 조회
     * @param familyId 가족 ID
     * @return 구성원 수
     */
    public int countFamilyMembers(String familyId) {
        // 더미 구현 - 테스트용 4명 고정
        return 4;
    }

    /**
     * 가족 구성원 ID 목록 조회
     * @param familyId 가족 ID
     * @return 구성원 ID 목록
     */
    public List<String> selectFamilyMemberIds(String familyId) {
        // 더미 구현 - 테스트용 ID 목록
        return new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
    }
}