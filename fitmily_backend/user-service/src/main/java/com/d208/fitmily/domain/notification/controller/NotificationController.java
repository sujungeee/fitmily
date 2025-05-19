package com.d208.fitmily.domain.notification.controller;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.notification.dto.NotificationListResponseDTO;
import com.d208.fitmily.domain.notification.dto.UnreadNotificationResponseDTO;
import com.d208.fitmily.domain.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 관련 API")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 목록 조회", description = "사용자의 알림 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<NotificationListResponseDTO> getNotifications(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        int userId = principal.getId();
        NotificationListResponseDTO response = notificationService.getNotifications(userId, page, size);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "읽지 않은 알림 정보 조회", description = "사용자의 읽지 않은 알림 정보를 조회합니다.")
    @GetMapping("/unread")
    public ResponseEntity<UnreadNotificationResponseDTO> getUnreadNotifications(
            @AuthenticationPrincipal CustomUserDetails principal) {

        int userId = principal.getId();
        UnreadNotificationResponseDTO response = notificationService.getUnreadNotifications(userId);

        return ResponseEntity.ok(response);
    }

//    @Operation(summary = "알림 읽음 처리", description = "특정 알림을 읽음 상태로 변경합니다.")
//    @PatchMapping("/{notificationId}/read")
//    public ResponseEntity<Map<String, Object>> markAsRead(
//            @AuthenticationPrincipal CustomUserDetails principal,
//            @PathVariable int notificationId) {
//
//        notificationService.markAsRead(notificationId);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("success", true);
//
//        return ResponseEntity.ok(response);
//    }
}