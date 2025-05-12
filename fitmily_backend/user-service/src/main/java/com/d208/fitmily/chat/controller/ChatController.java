package com.d208.fitmily.chat.controller;

import com.d208.fitmily.chat.dto.ChatMessagesResponseDTO;
import com.d208.fitmily.chat.service.ChatService;
import com.d208.fitmily.common.response.ApiResponse;
import com.d208.fitmily.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅 API", description = "채팅 송/수신")
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "메시지 조회")
    @GetMapping("/family/{familyId}/messages")
    public ApiResponse<ChatMessagesResponseDTO> getMessages(
            @PathVariable String familyId,
            @RequestParam(required = false) String before,
            @RequestParam(defaultValue = "20") int limit,
            @AuthenticationPrincipal CustomUserDetails principal) {

        Integer userId = principal.getId();
        ChatMessagesResponseDTO response = chatService.getMessages(familyId, userId.toString(), before, limit);
        return ApiResponse.ok(response, "메시지 조회 성공");
    }
}