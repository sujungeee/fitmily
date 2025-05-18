package com.d208.fitmily.domain.family.controller;

import com.d208.fitmily.domain.family.dto.*;
import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.service.FamilyService;
import com.d208.fitmily.global.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Tag(name = "패밀리 API", description = "패밀리 생성/가입, 조회 관련 API")
public class FamilyController {

    private final FamilyService familyService;
    private final JWTUtil jwtUtil;


    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest request) {
        int familyId = familyService.createFamily(request.getFamilyName());
        return ResponseEntity.ok(new CreateFamilyResponse(familyId));
    }

    @PostMapping("/join")
    public ResponseEntity<JoinFamilyResponse> joinFamily(
            @RequestBody JoinFamilyRequest request,
            Authentication authentication) {

        // Spring Security의 Authentication 객체 활용
        int userId;
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                // SecurityContext에서 사용자 정보 가져오기
                userId = Integer.parseInt(authentication.getName());
            } catch (NumberFormatException e) {
                throw new RuntimeException("사용자 ID 형식이 올바르지 않습니다");
            }
        } else {
            throw new RuntimeException("인증 정보가 없습니다");
        }

        // 패밀리 가입 처리 전 디버그 로깅 추가
        System.out.println("초대 코드: " + request.getFamilyInviteCode() + ", 사용자 ID: " + userId);

        int familyId = familyService.joinFamily(request.getFamilyInviteCode(), userId);

        JoinFamilyResponse response = new JoinFamilyResponse(
                new JoinFamilyResponse.FamilyData(familyId)
        );
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyDetailResponse> getFamily(@PathVariable int familyId) {
        Family family = familyService.getFamily(familyId);

        FamilyDetailResponse.FamilyData familyData = new FamilyDetailResponse.FamilyData(
                family.getFamilyName(),
                family.getFamilyInviteCode(),
                family.getFamilyPeople()  // 패밀리 인원 수 추가
        );

        return ResponseEntity.ok(new FamilyDetailResponse(familyData));
    }


    @GetMapping("/{familyId}/dashboard")
    public ResponseEntity<FamilyDashboardResponse> getFamilyDashboard(
            @PathVariable int familyId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // 날짜 파라미터가 없으면 오늘 날짜 사용
        String dateStr = (date != null)
                ? date.format(DateTimeFormatter.ISO_DATE)
                : LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        FamilyDashboardResponse response = familyService.getFamilyDashboard(familyId, dateStr);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{familyId}/health-status")
    public ResponseEntity<FamilyHealthStatusResponse> getFamilyHealthStatus(@PathVariable int familyId) {
        FamilyHealthStatusResponse response = familyService.getFamilyHealthStatus(familyId);
        return ResponseEntity.ok(response);
    }

}
