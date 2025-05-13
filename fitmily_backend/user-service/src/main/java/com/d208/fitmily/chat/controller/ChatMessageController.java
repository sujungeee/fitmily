package com.d208.fitmily.chat.controller;

import com.d208.fitmily.chat.dto.MessageRequestDTO;
import com.d208.fitmily.chat.dto.ReadReceiptRequestDTO;
import com.d208.fitmily.chat.service.ChatMessageService;
import com.d208.fitmily.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

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
            SimpMessageHeaderAccessor headerAccessor) {

        // 디버깅 로그 추가
        log.debug("메시지 수신 - familyId: {}, content: {}, type: {}",
                familyId, messageRequest.getContent(), messageRequest.getMessageType());

        // 인증 정보에서 userId 추출
        String userId = getUserIdFromHeaderAccessor(headerAccessor);
        if (userId != null) {
            log.info("메시지 전송 처리 - userId: {}, familyId: {}", userId, familyId);
            chatMessageService.sendMessage(familyId, messageRequest, userId);
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");

            // 테스트용: 기본 사용자 ID 4로 메시지 전송 계속
            log.warn("테스트용: 기본 사용자 ID 4로 메시지 전송 계속");
            chatMessageService.sendMessage(familyId, messageRequest, "4");
        }
    }

    @Operation(summary = "메시지 읽음 상태 업데이트", description = "WebSocket을 통해 메시지 읽음 상태를 업데이트합니다.")
    @MessageMapping("/chat.markAsRead/family/{familyId}")
    public void markAsRead(
            @DestinationVariable String familyId,
            @Payload ReadReceiptRequestDTO readReceiptRequest,
            SimpMessageHeaderAccessor headerAccessor) {

        log.debug("읽음 상태 수신 - familyId: {}, messageId: {}",
                familyId, readReceiptRequest.getMessageId());

        String userId = getUserIdFromHeaderAccessor(headerAccessor);
        if (userId != null) {
            log.info("읽음 상태 처리 - userId: {}, familyId: {}, messageId: {}",
                    userId, familyId, readReceiptRequest.getMessageId());
            chatMessageService.markAsRead(familyId, readReceiptRequest.getMessageId(), userId);
        } else {
            log.error("사용자 인증 정보를 찾을 수 없음");

            // 테스트용: 기본 사용자 ID 4로 읽음 처리 계속
            log.warn("테스트용: 기본 사용자 ID 4로 읽음 처리 계속");
            chatMessageService.markAsRead(familyId, readReceiptRequest.getMessageId(), "4");
        }
    }

    // 사용자 ID 추출 메서드 (통합된 버전)
    private String getUserIdFromHeaderAccessor(SimpMessageHeaderAccessor headerAccessor) {
        if (headerAccessor != null && headerAccessor.getUser() instanceof Authentication) {
            Authentication auth = (Authentication) headerAccessor.getUser();
            Object principal = auth.getPrincipal();
            log.debug("Principal 타입: {}", principal.getClass().getName());

            // CustomUserDetails 타입인 경우
            if (principal instanceof CustomUserDetails) {
                Integer id = ((CustomUserDetails) principal).getId();
                log.debug("CustomUserDetails에서 ID 추출: {}", id);
                return id.toString();
            }

            // principal이 직접 userId 문자열인 경우
            if (principal instanceof String) {
                return (String) principal;
            }

            // 리플렉션 시도
            try {
                if (principal.getClass().getMethod("getId") != null) {
                    Object id = principal.getClass().getMethod("getId").invoke(principal);
                    if (id != null) {
                        log.debug("리플렉션으로 ID 추출: {}", id);
                        return id.toString();
                    }
                }
            } catch (Exception e) {
                log.debug("getId() 메서드 호출 실패: {}", e.getMessage());
            }

            // 다른 Principal 타입 처리
            log.debug("다른 Principal 타입: {}", principal.getClass().getName());
            return principal.toString();
        }

        // 세션 ID 로깅
        log.debug("세션 ID: {}", headerAccessor.getSessionId());

        return null;
    }
}