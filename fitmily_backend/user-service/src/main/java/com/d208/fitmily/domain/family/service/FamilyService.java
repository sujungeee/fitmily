package com.d208.fitmily.domain.family.service;

import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyMapper familyMapper;

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
}