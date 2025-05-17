package com.d208.fitmily.global.config;

import com.d208.fitmily.global.handler.DelegatingStompHandler;
//import com.d208.fitmily.global.handler.StompHandler;
import com.d208.fitmily.global.handler.StompPrincipal;
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
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final DelegatingStompHandler delegatingStompHandler;
    private final JWTUtil jwtUtil;


    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketConfig.class);

//    private final StompHandler stompHandler;

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
        // WebSocket 연결 엔드포인트
        registry.addEndpoint("/api/ws-connect")
                .setAllowedOriginPatterns("*");
//                .withSockJS();
        log.info("WebSocket 연결 완료: /api/ws-connect");
    }

    // JWT 인증
    @Override
    // 컨트롤러로 가기전에 가로채는 인터셉터 등록 (여기서 JWT 인증)
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            //  STOMP 명령어, 헤더를 읽을 수 있게 래핑함
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);


                // CONNECT 명령일 때 JWT 인증 처리
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");

                    if (token != null && token.startsWith("Bearer ")) {
                        token = token.substring(7);

                        //validateToken JWT 유효성 검사
                        if (jwtUtil.validateToken(token)) {
                            Integer userId = jwtUtil.getUserId(token);
                            accessor.setUser(new StompPrincipal(userId.toString())); //WebSocket 세션에 사용자 정보 등록
                        } else {
                            throw new IllegalArgumentException("Invalid JWT token"); //토큰 유효하지 않으면 예외
                        }
                    } else {
                        throw new IllegalArgumentException("Missing Authorization header"); //헤더 없다면 예외
                    }
                }
                return message;
            }
        });
    }


}