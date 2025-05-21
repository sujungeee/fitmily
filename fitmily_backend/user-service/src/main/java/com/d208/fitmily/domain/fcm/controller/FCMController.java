package com.d208.fitmily.domain.fcm.controller;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.fcm.dto.FcmTokenRequest;
import com.d208.fitmily.domain.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM", description = "FCM 알림 관련 API")
public class FCMController {

    private final FcmService fcmService;

    @Operation(summary = "FCM 토큰 등록", description = "토큰을 등록합니다.")
    @PostMapping("/token")
    public ResponseEntity<Object> registerToken(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody FcmTokenRequest request) {

        // 인증된 사용자 정보 확인
        if (principal == null) {
            log.error("인증 정보가 없습니다. FCM 토큰 등록 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int userId = principal.getId();
        log.info("FCM 토큰 등록 요청: userId={}", userId);

        fcmService.registerToken(userId, request.getFcmToken());
        return ResponseEntity.ok().build();
    }
}