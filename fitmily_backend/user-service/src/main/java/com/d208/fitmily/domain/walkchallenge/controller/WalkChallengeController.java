package com.d208.fitmily.domain.walkchallenge.controller;

import com.d208.fitmily.domain.walkchallenge.dto.WalkChallengeResponseDto;
import com.d208.fitmily.domain.walkchallenge.service.WalkChallengeService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.walk.dto.UserDto;
import com.d208.fitmily.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Operation(summary = "이번 주 산책 챌린지 조회")
    @GetMapping("/challenge")
    public ResponseEntity<WalkChallengeResponseDto> getCurrentChallenge(@AuthenticationPrincipal CustomUserDetails principal) {
        Integer userId = principal.getId();
        UserDto userDto = userService.getUserDtoById(userId);
        Integer familyId = userDto.getFamilyId();

        if (familyId == null) {
            return ResponseEntity.ok(WalkChallengeResponseDto.builder()
                    .build());
        }

        WalkChallengeResponseDto challenge = walkChallengeService.getCurrentChallenge(familyId);

        if (challenge == null) {
            return ResponseEntity.ok(WalkChallengeResponseDto.builder()
                    .build());
        }

        return ResponseEntity.ok(challenge);
    }
}