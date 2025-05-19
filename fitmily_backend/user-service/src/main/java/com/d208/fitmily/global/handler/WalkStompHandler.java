package com.d208.fitmily.global.handler;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.global.common.exception.BusinessException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import com.d208.fitmily.global.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WalkStompHandler implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("구독 확인으로 들어옴");
            String destination = accessor.getDestination();
            String sessionId = accessor.getSessionId();

            System.out.println("📡 [SUBSCRIBE] 구독 요청 도착");
            System.out.println("📍 Destination: " + destination);
            System.out.println("👤 Session ID: " + sessionId);
        }

        System.out.println("accessor: " +accessor);

        // CONNECT일 경우 JWT 인증 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeader = accessor.getNativeHeader("Authorization");
            if (authHeader == null || authHeader.isEmpty()) {
                throw new IllegalArgumentException("Authorization header is missing");
            }

            String token = authHeader.get(0).replace("Bearer ", "");
            System.out.println("토큰: " +token);

            Integer userId = jwtUtil.getUserId(token);
            System.out.println("userId: " +userId);

            // 🔑 커스텀 Principal 생성
            System.out.println("생성완료");
            accessor.setLeaveMutable(true); // 안전하게 유지// 🔥 이거 안 하면 setUser 무시됨
            System.out.println(accessor.getId());
            ;
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());


        }

        return message;
    }
}


