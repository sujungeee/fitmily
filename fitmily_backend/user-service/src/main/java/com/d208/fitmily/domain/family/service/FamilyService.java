package com.d208.fitmily.domain.family.service;

import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyMapper familyMapper;
    private static final int MAX_FAMILY_MEMBERS = 6;

    @Transactional
    public int createFamily(String familyName) {
        Family family = new Family();
        family.setFamilyName(familyName);

        // 초대 코드 생성
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);
        family.setFamilyInviteCode(inviteCode);

        // 초기 설정
        family.setFamilyPeople(1); // 처음 생성 시 1명
        LocalDateTime now = LocalDateTime.now();
        family.setFamilyCreatedAt(now);
        family.setFamilyUpdatedAt(now);

        // DB에 저장
        familyMapper.createFamily(family);

        return family.getFamilyId();
    }

    /**
     * 패밀리 가입
     */
    @Transactional
    public int joinFamily(String inviteCode, int userId) {
        // 초대 코드로 패밀리 조회
        Family family = familyMapper.findByInviteCode(inviteCode);
        if (family == null) {
            throw new com.d208.fitmily.global.exception.CustomException(ErrorCode.INVALID_INVITE_CODE);
        }

        // 최대 인원 체크 (6명)
        if (family.getFamilyPeople() >= MAX_FAMILY_MEMBERS) {
            throw new com.d208.fitmily.global.exception.CustomException(ErrorCode.FAMILY_MEMBER_LIMIT_EXCEEDED);
        }

        // 사용자 패밀리 업데이트
        familyMapper.updateUserFamilyId(userId, family.getFamilyId());

        // 패밀리 인원 수 증가
        familyMapper.incrementFamilyPeople(family.getFamilyId());

        return family.getFamilyId();
    }

}