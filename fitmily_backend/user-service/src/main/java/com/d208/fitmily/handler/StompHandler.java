package com.d208.fitmily.handler;

import com.d208.fitmily.common.exception.BusinessException;
import com.d208.fitmily.common.exception.ErrorCode;
import com.d208.fitmily.jwt.JWTUtil;
import com.d208.fitmily.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결 시 JWT 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                if (!jwtUtil.validateToken(token)) {
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }
                Integer userId = jwtUtil.getUserId(token);
                String role = jwtUtil.getRole(token);

                CustomUserDetails userDetails = new CustomUserDetails(userId, role);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, Collections.singletonList(new SimpleGrantedAuthority(role))

                        SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
            } else {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }

        }

        // 채팅방 구독 처리
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/topic/chat/family/")) {
                String familyId = destination.replace("/topic/chat/family/", "");

                if (accessor.getUser() != null) {
                    if (accessor.getUser() instanceof Authentication) {
                        Authentication auth = (Authentication) accessor.getUser();
                        Object principal = auth.getPrincipal();

                        Integer userId;
                        if (principal instanceof CustomUserDetails) {
                            userId = ((CustomUserDetails) principal).getId();
                        } else if (principal instanceof Integer) {
                            userId = (Integer) principal;
                        } else {
                            userId = 1; // 기본값
                        }

                        // Redis에 구독자 목록에 추가
                        redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", userId.toString());
                    }
                }
            }
        }

        // 연결 종료 시
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            if (accessor.getUser() != null) {
                // Authentication으로 캐스팅하여 getPrincipal() 메서드 사용
                if (accessor.getUser() instanceof Authentication) {
                    Authentication auth = (Authentication) accessor.getUser();
                    Object principal = auth.getPrincipal();

                    Integer userId;
                    if (principal instanceof CustomUserDetails) {
                        userId = ((CustomUserDetails) principal).getId();
                    } else if (principal instanceof Integer) {
                        userId = (Integer) principal;
                    } else {
                        userId = 1; // 기본값
                    }

                    // Redis에 오프라인 상태 저장
                    redisTemplate.opsForValue().set("user:" + userId + ":status", "offline");
                    redisTemplate.opsForValue().set("user:" + userId + ":lastSeen", String.valueOf(System.currentTimeMillis()));
                }
            }
        }

        return message;
    }
}