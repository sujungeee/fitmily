package com.d208.fitmily.handler;

import com.d208.fitmily.common.exception.BusinessException;
import com.d208.fitmily.common.exception.ErrorCode;
import com.d208.fitmily.jwt.JWTUtil;
import com.d208.fitmily.user.dto.CustomUserDetails;
import com.d208.fitmily.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class WalkStompHandler {

    private final JWTUtil jwtUtil;

    public Message<?> handle(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        // 1️⃣ CONNECT 시: 인증 수행
        if (StompCommand.CONNECT.equals(command)) {
            System.out.println("🔥 [CONNECT] 요청 도착");

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            System.out.println("🧾 Authorization 헤더: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (!jwtUtil.validateToken(token)) {
                    System.out.println("❌ 유효하지 않은 토큰");
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }

                Integer userId = jwtUtil.getUserId(token);
                String role = jwtUtil.getRole(token);
                System.out.println("✅ 토큰 인증 성공 → userId: " + userId + ", role: " + role);

                // 사용자 정보 구성
                User user = new User();
                user.setUserId(userId);
                user.setRole(role);
                CustomUserDetails userDetails = new CustomUserDetails(user);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                );

                // ✅ 메시지에 인증 정보 삽입
                accessor.setUser(auth);
                System.out.println("🔐 setUser(auth) 설정 완료 → " + auth.getName());

                // ✅ 세션에 인증 정보 저장
                accessor.getSessionAttributes().put("user", auth);
                System.out.println("💾 sessionAttributes 저장 완료: sessionId = " + accessor.getSessionId());

            } else {
                System.out.println("❌ Authorization 헤더 누락 또는 형식 오류");
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }
        }

        // 2️⃣ SEND / SUBSCRIBE 시: 인증 정보 복원
        else if (StompCommand.SEND.equals(command) || StompCommand.SUBSCRIBE.equals(command)) {
            if (accessor.getUser() == null) {
                System.out.println("🧩 [SEND/SUB] getUser() == null → 세션에서 복원 시도");

                Object authObj = accessor.getSessionAttributes().get("user");

                if (authObj instanceof Authentication auth) {
                    accessor.setUser(auth);
                    System.out.println("✅ 인증 정보 복원 성공 → userId: " + auth.getName());
                } else {
                    System.out.println("❌ 인증 정보 복원 실패 → 세션에 없음");
                }
            } else {
                System.out.println("✅ getUser() 이미 존재: " + accessor.getUser().getName());
            }
        }

        return message;
    }
}
