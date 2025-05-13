package com.d208.fitmily.chat.controller;

import com.d208.fitmily.chat.dto.MessageRequestDTO;
import com.d208.fitmily.chat.service.ChatMessageService;
import com.d208.fitmily.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Operation(summary = "메시지 전송")
    @MessageMapping("/chat.sendMessage/family/{familyId}")
    public void sendMessage(
            @DestinationVariable String familyId,
            @Payload MessageRequestDTO messageRequest,
            SimpMessageHeaderAccessor headerAccessor) {

        // 인증 정보에서 userId 추출
        Integer userId = getUserIdFromHeaderAccessor(headerAccessor);
        if (userId != null) {
            chatMessageService.sendMessage(familyId, messageRequest, userId.toString());
        }
    }

    private Integer getUserIdFromHeaderAccessor(SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor != null && headerAccessor.getUser() instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) headerAccessor.getUser();
            if (token.getPrincipal() instanceof CustomUserDetails) {
                return ((CustomUserDetails) token.getPrincipal()).getId();
            }
        }
        return null;
    }
}