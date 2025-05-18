package com.d208.fitmily.domain.family.controller;

import com.d208.fitmily.domain.family.dto.*;
import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.service.FamilyService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.global.jwt.JWTUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        int userId;

        // 1. CustomUserDetails에서 사용자 ID 가져오기 시도
        if (principal != null) {
            userId = principal.getId();
            System.out.println("사용자 ID (principal에서): " + userId);
        }
        // 2. Authorization 헤더가 있으면 JWT에서 추출 시도
        else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                userId = jwtUtil.getUserId(token);
                System.out.println("사용자 ID (JWT에서): " + userId);
            } catch (Exception e) {
                // 3. 테스트용 하드코딩 ID 사용
                userId = 2;  // 테스트용 사용자 ID
                System.out.println("사용자 ID (하드코딩): " + userId);
            }
        }
        // 4. 둘 다 없으면 테스트용 ID 사용
        else {
            userId = 2;  // 테스트용 사용자 ID
            System.out.println("사용자 ID (하드코딩): " + userId);
        }

        int familyId = familyService.joinFamily(request.getFamilyInviteCode(), userId);

        return ResponseEntity.ok(new JoinFamilyResponse(
                new JoinFamilyResponse.FamilyData(familyId)
        ));
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
