package com.d208.fitmily.global.handler;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegatingStompHandler implements ChannelInterceptor {

    private final WalkStompHandler walkStompHandler;
    private final ChatStompHandler chatStompHandler;

    private static final Logger log = LoggerFactory.getLogger(DelegatingStompHandler.class);

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();

        // 메시지 목적지 확인
        String destination = accessor.getDestination();
        log.info("STOMP 메시지 - command: {}, sessionId: {}, destination: {}",
                command, sessionId, destination);

        try {
            // CONNECT 시 인증은 WebSocketConfig에서 이미 처리됨
            // 여기서는 데스티네이션 기반으로만 라우팅
            if (StompCommand.CONNECT.equals(command)) {
                log.info("CONNECT 처리: sessionId={}", sessionId);

                // CONNECT 시에는 두 핸들러 모두 실행
                try {
                    chatStompHandler.handle(message, channel);
                } catch (Exception e) {
                    log.error("채팅 핸들러 실행 오류: {}", e.getMessage(), e);
                }

                try {
                    walkStompHandler.handle(message, channel);
                } catch (Exception e) {
                    log.error("산책 핸들러 실행 오류: {}", e.getMessage(), e);
                }

                return message;
            }

            // SUBSCRIBE/SEND 시 목적지에 따라 라우팅
            if ((StompCommand.SUBSCRIBE.equals(command) || StompCommand.SEND.equals(command))
                    && destination != null) {
                if (destination.contains("chat")) {
                    log.info("Chat 메시지 라우팅: {}", destination);
                    return chatStompHandler.handle(message, channel);
                } else if (destination.contains("walk")) {
                    log.info("Walk 메시지 라우팅: {}", destination);
                    return walkStompHandler.handle(message, channel);
                }
            }

            // 인증 정보 설정
            if (accessor.getUser() instanceof Authentication auth) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("메시지 처리 중 오류: {}", e.getMessage(), e);
        }

        return message;
    }
}