package com.d208.fitmily.domain.fcm.controller;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.fcm.dto.FcmTokenRequest;
import com.d208.fitmily.domain.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
@Tag(name = "FCM", description = "FCM 알림 관련 API")
public class FCMController {

    private final FcmService fcmService;

    @Operation(summary = "FCM 토큰 등록", description = "Firebase Cloud Messaging 토큰을 등록합니다.")
    @PostMapping("/token")
    public ResponseEntity<Map<String, Object>> registerToken(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody FcmTokenRequest request) {

        int userId = principal.getId();
        log.info("FCM 토큰 등록 요청: userId={}", userId);

        fcmService.registerToken(userId, request.getFcmToken());

        Map<String, Object> emptyResponse = new HashMap<>();

        return ResponseEntity.ok(emptyResponse);
    }
}