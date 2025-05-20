package com.d208.fitmily.global.handler;

import com.d208.fitmily.domain.chat.service.ChatSessionService;
import com.d208.fitmily.global.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ChatStompHandler {

    private final JWTUtil jwtUtil;
    private final ChatSessionService chatSessionService;
    private static final Logger log = LoggerFactory.getLogger(ChatStompHandler.class);

    public Message<?> handle(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        // CONNECT 시 인증 처리
        if (StompCommand.CONNECT.equals(command)) {
            log.debug("채팅 CONNECT 요청 처리: sessionId={}", sessionId);
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (jwtUtil.validateToken(token)) {
                    Integer userId = jwtUtil.getUserId(token);
                    String role = jwtUtil.getRole(token);

                    // 인증 객체 생성
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            userId.toString(), null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    );

                    // 세션에 인증 정보 저장
                    accessor.setUser(auth);
                    accessor.getSessionAttributes().put("userId", userId.toString());
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    // 세션 정보 저장
                    chatSessionService.saveSessionInfo(sessionId, userId.toString());

                    log.debug("채팅 인증 성공: sessionId={}, userId={}", sessionId, userId);
                }
            }
        }
        // SUBSCRIBE 시 온라인 상태 등록
        else if (StompCommand.SUBSCRIBE.equals(command) && destination != null) {
            if (destination.startsWith("/topic/chat/family/")) {
                String familyId = destination.substring("/topic/chat/family/".length());
                String userId = extractUserId(accessor);

                if (userId != null) {
                    // 채팅방 온라인 사용자 등록
                    chatSessionService.registerOnlineUser(familyId, userId);
                    log.info("채팅방 구독: userId={}, familyId={}", userId, familyId);
                }
            }
        }
        // UNSUBSCRIBE 시 온라인 상태 해제
        else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            String userId = extractUserId(accessor);
            String subscriptionId = accessor.getSubscriptionId();

            log.debug("채팅 UNSUBSCRIBE: userId={}, subscriptionId={}", userId, subscriptionId);

            if (userId != null) {
                // 모든 채팅방에서 사용자 제거 (정확한 채팅방 ID를 알 수 없으므로)
                chatSessionService.unregisterOnlineUser(null, userId);
            }
        }
        // DISCONNECT 시 온라인 상태 해제
        else if (StompCommand.DISCONNECT.equals(command)) {
            log.debug("채팅 DISCONNECT: sessionId={}", sessionId);
            chatSessionService.removeSessionInfo(sessionId);
        }
        // SEND 또는 기타 명령 시 인증 정보 설정
        else {
            if (accessor.getUser() instanceof Authentication auth) {
                SecurityContextHolder.getContext().setAuthentication(auth);
                log.debug("채팅 인증 정보 설정: {}", auth.getName());
            }
        }

        return message;
    }

    // 사용자 ID 추출 메서드
    private String extractUserId(StompHeaderAccessor accessor) {
        // 세션 속성에서 userId 바로 추출
        if (accessor.getSessionAttributes() != null) {
            Object userIdAttr = accessor.getSessionAttributes().get("userId");
            if (userIdAttr instanceof String) {
                return (String) userIdAttr;
            }
        }

        // Authentication에서 추출
        if (accessor.getUser() != null) {
            return accessor.getUser().getName();
        }

        // 세션 ID로 조회
        String sessionId = accessor.getSessionId();
        if (sessionId != null) {
            return chatSessionService.getUserIdFromSession(sessionId);
        }

        return null;
    }
}