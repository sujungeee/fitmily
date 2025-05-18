package com.d208.fitmily.domain.family.controller;

import com.d208.fitmily.domain.family.dto.*;
import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.service.FamilyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Tag(name = "패밀리 API", description = "패밀리 생성/가입, 조회 관련 API")
public class FamilyController {

    private final FamilyService familyService;

    @PostMapping
    public ResponseEntity<CreateFamilyResponse> createFamily(@RequestBody CreateFamilyRequest request) {
        int familyId = familyService.createFamily(request.getFamilyName());
        return ResponseEntity.ok(new CreateFamilyResponse(familyId));
    }

    @PostMapping("/join")
    public ResponseEntity<JoinFamilyResponse> joinFamily(@RequestBody JoinFamilyRequest request,
                                                         Authentication authentication) {
        // Authentication 객체에서 직접 사용자 ID 추출
        int userId;
        if (authentication != null && authentication.isAuthenticated()) {
            try {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    userId = Integer.parseInt(((UserDetails) principal).getUsername());
                } else if (!"anonymousUser".equals(principal)) {
                    userId = Integer.parseInt(authentication.getName());
                } else {
                    throw new RuntimeException("로그인이 필요합니다");
                }
            } catch (NumberFormatException e) {
                throw new RuntimeException("사용자 ID 형식이 올바르지 않습니다");
            }
        } else {
            throw new RuntimeException("인증 정보가 없습니다");
        }

        // 패밀리 가입 처리
        int familyId = familyService.joinFamily(request.getFamilyInviteCode(), userId);

        // 응답 생성
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
