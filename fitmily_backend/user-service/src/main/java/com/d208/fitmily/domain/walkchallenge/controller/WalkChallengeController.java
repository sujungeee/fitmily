package com.d208.fitmily.domain.walkchallenge.controller;

import com.d208.fitmily.domain.walkchallenge.dto.WalkChallengeDto;
import com.d208.fitmily.domain.walkchallenge.dto.WalkChallengeResponseDto;
import com.d208.fitmily.domain.walkchallenge.mapper.WalkChallengeMapper;
import com.d208.fitmily.domain.walkchallenge.service.WalkChallengeService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.walk.dto.UserDto;
import com.d208.fitmily.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "산책 챌린지 API", description = "가족 산책 챌린지 관련 API")
@Slf4j
public class WalkChallengeController {

    private final WalkChallengeService walkChallengeService;
    private final UserService userService;
    private final WalkChallengeMapper walkChallengeMapper;

    @Operation(summary = "이번 주 산책 챌린지 조회")
    @GetMapping("/challenge")
    public ResponseEntity<WalkChallengeResponseDto> getCurrentChallenge(@AuthenticationPrincipal CustomUserDetails principal) {
        try {
            Integer userId = principal.getId();
            UserDto userDto = userService.getUserDtoById(userId);
            Integer familyId = userDto.getFamilyId();

            // 가족이 없는 경우 빈 응답 반환
            if (familyId == null) {
                log.info("사용자 {}는 가족에 속해있지 않습니다", userId);
                return ResponseEntity.ok(WalkChallengeResponseDto.builder().build());
            }

            WalkChallengeResponseDto challenge = walkChallengeService.getCurrentChallenge(familyId);
            return ResponseEntity.ok(challenge != null ? challenge : WalkChallengeResponseDto.builder().build());
        } catch (Exception e) {
            log.error("챌린지 조회 중 오류: {}", e.getMessage(), e);
            return ResponseEntity.ok(WalkChallengeResponseDto.builder().build());
        }
    }

    @Operation(summary = "[개발용] 챌린지 종료 알림 테스트")
    @PostMapping("/challenge/test-notification/{familyId}")
    public ResponseEntity<Map<String, Object>> testChallengeCompletion(
            @PathVariable Integer familyId,
            @AuthenticationPrincipal CustomUserDetails principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 현재 활성화된 챌린지 정보 가져오기
            WalkChallengeResponseDto challengeResponse = walkChallengeService.getCurrentChallenge(familyId);

            if (challengeResponse == null || challengeResponse.getChallengeId() == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "활성화된 챌린지가 없습니다");
                return ResponseEntity.ok(response);
            }

            // 원본 챌린지 DTO 조회
            WalkChallengeDto challenge = walkChallengeMapper.findByIdWithoutRelations(challengeResponse.getChallengeId());

            if (challenge == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "챌린지 정보를 찾을 수 없습니다");
                return ResponseEntity.ok(response);
            }

            // 목표 거리의 75% 달성했다고 가정 (테스트용)
            float totalDistance = challenge.getTargetDistance() * 0.75f;

            // 강제로 챌린지 종료 알림 발송
            walkChallengeService.testChallengeCompletion(familyId, challenge, totalDistance);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "테스트용 챌린지 종료 알림 발송 완료");
            response.put("challengeId", challenge.getChallengeId());
            response.put("targetDistance", challenge.getTargetDistance());
            response.put("totalDistance", totalDistance);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("테스트용 챌린지 종료 알림 발송 중 오류: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "알림 발송 중 오류 발생: " + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
}