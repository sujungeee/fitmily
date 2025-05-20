package com.d208.fitmily.global.config;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.global.handler.DelegatingStompHandler;
//import com.d208.fitmily.global.handler.StompPrincipal;
import com.d208.fitmily.global.handler.WalkStompHandler;
import com.d208.fitmily.global.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.Collections;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final DelegatingStompHandler delegatingStompHandler;
    private final JWTUtil jwtUtil;
    private final WalkStompHandler walkStompHandler;


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독하기
        registry.enableSimpleBroker("/topic");
        // 메세지보내기
        registry.setApplicationDestinationPrefixes("/app");
        // 사용자 지정 수신 (/user/{username}/queue/messages)
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 일반 WebSocket 연결 엔드포인트
        registry.addEndpoint("/api/ws-connect")
                .setAllowedOriginPatterns("*");

        // SockJS 지원 엔드포인트
        registry.addEndpoint("/api/ws-connect")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setSessionCookieNeeded(false);

        log.info("WebSocket 연결 완료: /api/ws-connect");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // CONNECT 명령일 때만 처리
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    log.info("STOMP 연결 요청 처리");

                    // JWT 토큰 처리
                    String authHeader = accessor.getFirstNativeHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        try {
                            // 토큰 검증 및 사용자 정보 추출
                            String token = authHeader.substring(7);
                            if (jwtUtil.validateToken(token)) {
                                Integer userId = jwtUtil.getUserId(token);
                                String role = jwtUtil.getRole(token);

                                // 간단한 Authentication 객체 생성
                                Authentication auth = new UsernamePasswordAuthenticationToken(
                                        userId.toString(), null,
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                                );

                                // 사용자 정보 설정
                                accessor.setUser(auth);
                                accessor.getSessionAttributes().put("userId", userId.toString());

                                log.info("WebSocket 인증 성공: userId={}", userId);
                            }
                        } catch (Exception e) {
                            log.warn("인증 처리 실패: {}", e.getMessage());
                        }
                    }
                }
                return message;
            }
        });
    }
}