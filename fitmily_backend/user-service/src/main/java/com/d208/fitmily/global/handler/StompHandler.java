package com.d208.fitmily.global.handler;

import com.d208.fitmily.global.common.exception.BusinessException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import com.d208.fitmily.global.jwt.JWTUtil;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 디버깅 로깅 추가
//        System.out.println("STOMP Command: " + accessor.getCommand());

        // 메서드 전체에서 사용할 sessionId 변수
        String sessionId = accessor.getSessionId();

        // 이미 인증된 사용자 정보 복원 (이 부분이 중요합니다!)
        if (accessor.getUser() == null &&
                (StompCommand.SUBSCRIBE.equals(accessor.getCommand()) ||
                        StompCommand.SEND.equals(accessor.getCommand()))) {
            System.out.println("🔄 사용자 인증 정보 복원 시도");

            Object authObj = accessor.getSessionAttributes().get("user");

            if (authObj instanceof Authentication auth) {
                accessor.setUser(auth);
                System.out.println("✅ 사용자 인증 정보 복원 성공");
            } else {
                System.out.println("❌ 사용자 인증 정보 복원 실패 (세션에 없음)");
            }

            // Redis에서 세션 ID로 사용자 정보 조회 (새로 추가)
            String userIdStr = (String) redisTemplate.opsForValue().get("ws:session:" + sessionId);
            if (userIdStr != null) {
                Integer userId = Integer.parseInt(userIdStr);

                // 사용자 정보 생성 및 설정
                User user = new User();
                user.setUserId(userId);
                user.setRole("ROLE_USER");

                CustomUserDetails userDetails = new CustomUserDetails(user);
                Collection<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, authorities);

                accessor.setUser(auth);
            } else {
                System.out.println("Redis에서 사용자 정보를 찾을 수 없음: " + sessionId);
            }
        }

        // WebSocket 연결 시 JWT 토큰 검증
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("🔥 STOMP CONNECT 도착");

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            System.out.println("🧾 Authorization 헤더: " + authHeader);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                System.out.println("🔓 토큰 추출: " + token.substring(0, 10) + "...");

                if (!jwtUtil.validateToken(token)) {
                    throw new BusinessException(ErrorCode.INVALID_TOKEN);
                }

                Integer userId = jwtUtil.getUserId(token);
                String role = jwtUtil.getRole(token);
                System.out.println("✅ 토큰 OK, userId: " + userId + ", role: " + role);

                User user = new User();
                user.setUserId(userId);
                user.setPassword("temppassword"); // 비밀번호는 의미 없음
                user.setRole(role);

                CustomUserDetails userDetails = new CustomUserDetails(user);

                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                Collection<SimpleGrantedAuthority> authorities = Collections.singletonList(authority);


                // 인증 객체를 생성하는거임 그리고 이 객체를 넘기기
                Authentication auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);

                accessor.getSessionAttributes().put("user", auth);
                System.out.println("✅ WebSocket 세션에 인증 정보 저장 완료");

                // Redis에 세션 ID와 사용자 ID 매핑 저장 (새로 추가)
                redisTemplate.opsForValue().set("ws:session:" + sessionId, userId.toString());
            } else {
                System.out.println("인증 헤더 없음 또는 형식 불일치: " + authHeader);
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }
        }


        // 채팅방 구독 처리
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            System.out.println("구독 요청: " + destination);
            if (destination != null && destination.startsWith("/topic/chat/family/")) {
                String familyId = destination.replace("/topic/chat/family/", "");
                System.out.println("가족 ID: " + familyId);

                if (accessor.getUser() != null) {
                    System.out.println("사용자 정보: " + accessor.getUser());

                    if (accessor.getUser() instanceof Authentication) {
                        Authentication auth = (Authentication) accessor.getUser();
                        Object principal = auth.getPrincipal();
                        System.out.println("Principal 타입: " + principal.getClass().getName());

                        Integer userId;
                        if (principal instanceof CustomUserDetails) {
                            userId = ((CustomUserDetails) principal).getId();
                            System.out.println("CustomUserDetails에서 ID 추출: " + userId);
                        } else if (principal instanceof Integer) {
                            userId = (Integer) principal;
                            System.out.println("Integer에서 ID 추출: " + userId);
                        } else {
                            userId = 1; // 기본값
                            System.out.println("기본 ID 사용: " + userId);
                        }

                        // Redis에 구독자 목록에 추가
                        redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", userId.toString());
                        System.out.println("Redis에 구독자 추가: family:" + familyId + ":subscribers, " + userId);
                    } else {
                        System.out.println("User 객체가 Authentication 타입이 아님: " + accessor.getUser().getClass().getName());
                    }
                } else {
                    System.out.println("사용자 정보 없음");
                }
            }
        }

        // 메시지 전송 처리 추가 (디버깅)
        else if (StompCommand.SEND.equals(accessor.getCommand())) {
            System.out.println("메시지 전송 요청: " + accessor.getDestination());
//            System.out.println("인증 정보: " + (accessor.getUser() != null ? accessor.getUser() : "없음"));
        }

        // 연결 종료 시
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            System.out.println("‼️‼️‼️연결 종료 요청️‼️‼️️‼️‼️");

            if (accessor.getUser() != null) {
                System.out.println("사용자 정보: " + accessor.getUser());

                // Authentication으로 캐스팅하여 getPrincipal() 메서드 사용
                if (accessor.getUser() instanceof Authentication) {
                    Authentication auth = (Authentication) accessor.getUser();
                    Object principal = auth.getPrincipal();
                    System.out.println("Principal 타입: " + principal.getClass().getName());

                    Integer userId;
                    if (principal instanceof CustomUserDetails) {
                        userId = ((CustomUserDetails) principal).getId();
                        System.out.println("CustomUserDetails에서 ID 추출: " + userId);
                    } else if (principal instanceof Integer) {
                        userId = (Integer) principal;
                        System.out.println("Integer에서 ID 추출: " + userId);
                    } else {
                        userId = 1; // 기본값
                        System.out.println("기본 ID 사용: " + userId);
                    }

                    // Redis에 오프라인 상태 저장
                    redisTemplate.opsForValue().set("user:" + userId + ":status", "offline");
                    redisTemplate.opsForValue().set("user:" + userId + ":lastSeen", String.valueOf(System.currentTimeMillis()));

                    // Redis에서 세션 정보 삭제
                    redisTemplate.delete("ws:session:" + sessionId);
                    System.out.println("Redis에서 세션 정보 삭제: " + sessionId);
                }
            }
        }

        return message;
    }
}