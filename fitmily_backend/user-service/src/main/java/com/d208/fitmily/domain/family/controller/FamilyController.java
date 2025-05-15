package com.d208.fitmily.domain.family.controller;

import com.d208.fitmily.domain.family.dto.*;
import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.service.FamilyService;
import com.d208.fitmily.global.config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest request) {
        int familyId = familyService.createFamily(request.getFamilyName());
        return ResponseEntity.ok(new CreateFamilyResponse(familyId));
    }

    @PostMapping("/join")
    public ResponseEntity<JoinFamilyResponse> joinFamily(@RequestBody JoinFamilyRequest request) {
        // 현재 로그인한 사용자 ID 가져오기
        int userId = SecurityConfig.getCurrentUserId();

        // 패밀리 가입 처리
        int familyId = familyService.joinFamily(request.getFamilyInviteCode(), userId);

        // 응답 생성
        JoinFamilyResponse response = new JoinFamilyResponse(new JoinFamilyResponse.FamilyData(familyId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyDetailResponse> getFamily(@PathVariable int familyId) {
        Family family = familyService.getFamily(familyId);

        FamilyDetailResponse.FamilyData familyData = new FamilyDetailResponse.FamilyData(
                family.getFamilyName(),
                family.getFamilyInviteCode()
        );

        return ResponseEntity.ok(new FamilyDetailResponse(familyData));
    }

}
