package com.d208.fitmily.domain.chat.controller;

import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.service.ChatService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "채팅 API", description = "채팅 메시지 조회 및 관리")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "메시지 목록 조회", description = "가족 채팅방의 메시지 목록을 조회합니다.")
    @GetMapping("/family/{familyId}/messages")
    public ResponseEntity<ChatMessagesResponseDTO> getMessages(
            @PathVariable String familyId,
            @RequestParam(required = false) String before,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {

        // 인증 객체에서 사용자 ID 추출
        String userId = extractUserId(authentication);
        log.debug("메시지 조회 요청 - familyId: {}, userId: {}, before: {}, limit: {}",
                familyId, userId, before, limit);

        ChatMessagesResponseDTO response = chatService.getMessages(familyId, userId, before, limit);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "안 읽은 메시지 수 조회", description = "특정 가족 채팅방의 안 읽은 메시지 수를 조회합니다.")
    @GetMapping("/family/{familyId}/unread")
    public ResponseEntity<Integer> getUnreadCount(
            @PathVariable String familyId,
            Authentication authentication) {

        String userId = extractUserId(authentication);
        log.debug("안 읽은 메시지 수 조회 요청 - familyId: {}, userId: {}", familyId, userId);

        int unreadCount = chatService.getUnreadCount(familyId, userId);
        return ResponseEntity.ok(unreadCount);
    }

    // 사용자 ID 추출 메서드
    private String extractUserId(Authentication authentication) {
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            log.debug("Principal 타입: {}", principal.getClass().getName());

            // CustomUserDetails 타입인 경우
            if (principal instanceof CustomUserDetails) {
                return String.valueOf(((CustomUserDetails) principal).getId());
            }

            // 문자열 타입인 경우
            if (principal instanceof String) {
                return (String) principal;
            }

            // 다른 타입인 경우 toString() 결과에서 ID만 추출 시도
            String principalStr = principal.toString();
            if (principalStr.contains("CustomUserDetails")) {
                try {
                    // CustomUserDetails에서 ID 추출 시도
                    if (principal instanceof CustomUserDetails) {
                        return String.valueOf(((CustomUserDetails) principal).getId());
                    }
                } catch (Exception e) {
                    log.warn("CustomUserDetails에서 ID 추출 실패", e);
                }
            }

            return principalStr;
        }

        return null;
    }
}