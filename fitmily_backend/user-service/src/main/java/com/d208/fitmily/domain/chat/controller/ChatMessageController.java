package com.d208.fitmily.domain.chat.controller;

import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
import com.d208.fitmily.domain.chat.dto.ReadReceiptRequestDTO;
import com.d208.fitmily.domain.chat.service.ChatMessageService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @Operation(summary = "메시지 전송", description = "WebSocket을 통해 메시지를 전송합니다.")
    @MessageMapping("/chat.sendMessage/family/{familyId}")
    public void sendMessage(
            @DestinationVariable String familyId,
            @Payload MessageRequestDTO messageRequest,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("메시지 수신 - familyId: {}, content: {}, type: {}",
                familyId, messageRequest.getContent(), messageRequest.getMessageType());

        // 사용자 ID 추출
        String userId = extractUserIdEffectively(principal, headerAccessor);

        if (userId != null) {
            log.info("메시지 전송 처리 - userId: {}, familyId: {}", userId, familyId);

            // 권한 검증 후 메시지 전송
            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                chatMessageService.sendMessage(familyId, messageRequest, userId);
            } else {
                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
            }
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");
        }
    }

    @Operation(summary = "메시지 읽음 상태 업데이트", description = "WebSocket을 통해 메시지 읽음 상태를 업데이트합니다.")
    @MessageMapping("/chat.markAsRead/family/{familyId}")
    public void markAsRead(
            @DestinationVariable String familyId,
            @Payload ReadReceiptRequestDTO readReceiptRequest,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor) {

        log.info("읽음 상태 수신 - familyId: {}, messageId: {}",
                familyId, readReceiptRequest.getMessageId());

        String userId = extractUserIdEffectively(principal, headerAccessor);

        if (userId != null) {
            log.info("읽음 상태 처리 - userId: {}, familyId: {}, messageId: {}",
                    userId, familyId, readReceiptRequest.getMessageId());

            if (chatMessageService.validateUserFamilyAccess(userId, familyId)) {
                chatMessageService.markAsRead(familyId, readReceiptRequest.getMessageId(), userId);
            } else {
                log.error("사용자가 해당 패밀리에 접근할 권한이 없습니다 - userId: {}, familyId: {}", userId, familyId);
            }
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");
        }
    }

    // 통합된 사용자 ID 추출 메서드
    private String extractUserIdEffectively(Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        // 디버깅을 위한 로그
        if (principal != null) {
            log.debug("Principal 유형: {}", principal.getClass().getName());
        }

        if (headerAccessor != null && headerAccessor.getUser() != null) {
            log.debug("HeaderAccessor User 유형: {}", headerAccessor.getUser().getClass().getName());
        }

        // 1. SessionAttributes에서 직접 userId 가져오기 (가장 안정적인 방법)
        if (headerAccessor != null && headerAccessor.getSessionAttributes() != null) {
            Object userIdAttr = headerAccessor.getSessionAttributes().get("userId");
            if (userIdAttr instanceof String) {
                return (String) userIdAttr;
            }
        }

        // 2. Authentication에서 CustomUserDetails 추출
        if (principal instanceof Authentication auth) {
            Object principalObj = auth.getPrincipal();

            if (principalObj instanceof CustomUserDetails) {
                Integer id = ((CustomUserDetails) principalObj).getId();
                return id != null ? id.toString() : null;
            } else if (principalObj instanceof String) {
                return (String) principalObj;
            } else if (principalObj != null) {
                // ID 값만 추출하는 로직
                String fullStr = principalObj.toString();

                // CustomUserDetails에서 ID 값만 추출 시도
                if (fullStr.contains("CustomUserDetails")) {
                    try {
                        // 로그 출력
                        log.debug("CustomUserDetails에서 ID 추출 시도: {}", fullStr);
                        return String.valueOf(((CustomUserDetails) principalObj).getId());
                    } catch (Exception e) {
                        log.warn("CustomUserDetails에서 ID 변환 실패", e);
                    }
                }

                return fullStr;
            }
        }

        // 3. 일반 Principal
        if (principal != null) {
            return principal.getName();
        }

        // 4. HeaderAccessor에서 마지막 시도
        if (headerAccessor != null && headerAccessor.getUser() != null) {
            return headerAccessor.getUser().getName();
        }

        // ID를 찾을 수 없음
        return null;
    }
}