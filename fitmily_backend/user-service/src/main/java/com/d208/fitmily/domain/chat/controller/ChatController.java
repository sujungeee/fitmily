package com.d208.fitmily.domain.chat.controller;

import com.d208.fitmily.domain.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.domain.chat.service.ChatMessageService;
import com.d208.fitmily.domain.chat.service.ChatService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@Tag(name = "채팅 API", description = "채팅 메시지 조회")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "메시지 목록 조회", description = "가족 채팅방의 메시지 목록을 페이지 단위로 조회합니다.")
    @GetMapping("/family/{familyId}/{page}/messages")
    public ResponseEntity<ChatMessagesResponseDTO> getMessagesByPage(
            @PathVariable String familyId,
            @PathVariable int page,
            @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal CustomUserDetails principal) {

        try {
            if (principal == null) {
                log.warn("인증된 사용자 정보가 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Integer userId = principal.getId();
            if (userId == null) {
                log.warn("사용자 ID가 null입니다.");
                return ResponseEntity.badRequest().build();
            }

            log.debug("페이지 기반 메시지 조회 - familyId: {}, userId: {}, page: {}, limit: {}",
                    familyId, userId, page, limit);

            // 권한 확인
            if (!chatMessageService.validateUserFamilyAccess(userId.toString(), familyId)) {
                log.warn("사용자 {}가 가족 {}의 채팅방에 접근할 권한이 없습니다.", userId, familyId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            ChatMessagesResponseDTO response = chatService.getMessagesByPage(familyId, userId.toString(), page, limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("메시지 조회 API 오류", e);
            return ResponseEntity.ok(new ChatMessagesResponseDTO(Collections.emptyList(), false));
        }
    }
}