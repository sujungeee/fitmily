package com.d208.fitmily.global.config;

import com.d208.fitmily.global.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final DelegatingStompHandler delegatingStompHandler;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketConfig.class);

    private final StompHandler stompHandler;

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

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // STOMP 메시지 처리 전 인터셉터 등록
        registration.interceptors(delegatingStompHandler);
        System.out.println("인터셉터 등록 완료");
    }
}