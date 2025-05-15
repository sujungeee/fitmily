//package com.d208.fitmily.fcm.controller;
//
//import com.d208.fitmily.common.response.ApiResponse;
//import com.d208.fitmily.fcm.dto.FCMTokenRequestDTO;
//import com.d208.fitmily.fcm.service.FCMService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//
//@RestController
//@RequestMapping("/api/fcm")
//@RequiredArgsConstructor
//@Tag(name = "FCM", description = "FCM 알림 관련 API")
//public class FCMController {
//
//    private final FCMService fcmService;
//
//    @Operation(summary = "FCM 토큰 등록")
//    @PostMapping("/token")
//    public ApiResponse<Void> registerToken(
//            @RequestBody FCMTokenRequestDTO request,
//            @AuthenticationPrincipal Principal principal) {
//
//        String userId = principal.getName();
//        fcmService.registerFCMToken(userId, request);
//        return ApiResponse.ok(null, "FCM 토큰 등록 성공");
//    }
//}