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
@Tag(name = "채팅 API", description = "채팅 메시지 조회")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "메시지 목록 조회", description = "가족 채팅방의 메시지 목록을 페이지 단위로 조회합니다.")
    @GetMapping("/family/{familyId}/{page}/messages")
    public ResponseEntity<ChatMessagesResponseDTO> getMessagesByPage(
            @PathVariable String familyId,
            @PathVariable int page,
            @RequestParam(defaultValue = "20") int limit,
            Authentication authentication) {

        String userId = extractUserId(authentication);
        log.debug("페이지 기반 메시지 조회 - familyId: {}, userId: {}, page: {}, limit: {}",
                familyId, userId, page, limit);

        if (userId == null) {
            log.warn("사용자 ID를 추출할 수 없습니다. 인증 정보를 확인하세요.");
            return ResponseEntity.badRequest().build();
        }

        ChatMessagesResponseDTO response = chatService.getMessagesByPage(familyId, userId, page, limit);
        return ResponseEntity.ok(response);
    }

    // 사용자 ID 추출 메서드
    private String extractUserId(Authentication authentication) {
        if (authentication == null) {
            log.warn("인증 정보가 없습니다");
            return null;
        }

        try {
            Object principal = authentication.getPrincipal();
            log.debug("Principal 타입: {}", principal.getClass().getName());

            // CustomUserDetails 타입인 경우
            if (principal instanceof CustomUserDetails) {
                Integer id = ((CustomUserDetails) principal).getId();
                return id != null ? id.toString() : null;
            }

            // 문자열 타입인 경우 (토큰 인증 등)
            if (principal instanceof String) {
                return (String) principal;
            }

            // 그 외 타입의 경우 toString 시도
            if (principal != null) {
                return principal.toString();
            }
        } catch (Exception e) {
            log.error("사용자 ID 추출 중 오류 발생: {}", e.getMessage(), e);
        }

        return null;
    }
}