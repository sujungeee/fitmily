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

        // 메시지 목적지 확인
        String destination = accessor.getDestination();
        log.debug("STOMP 메시지 - command: {}, destination: {}", command, destination);

        try {
            // CONNECT 시 기본적으로 WalkStompHandler로 처리
            if (StompCommand.CONNECT.equals(command)) {
                // 헤더에서 연결 타입 확인 (채팅용 커스텀 헤더 추가 가능)
                String connectionType = accessor.getFirstNativeHeader("Connection-Type");
                log.debug("CONNECT 요청: connectionType={}", connectionType);

                if ("chat".equals(connectionType)) {
                    return chatStompHandler.handle(message, channel);
                } else {
                    return walkStompHandler.handle(message, channel);
                }
            }

            // SUBSCRIBE/SEND 시 목적지에 따라 라우팅
            if ((StompCommand.SUBSCRIBE.equals(command) || StompCommand.SEND.equals(command))
                    && destination != null) {

                // 부분 문자열 포함 여부로 확인 (더 관대한 매칭)
                if (destination.contains("chat")) {
                    log.debug("Chat 메시지 라우팅: {}", destination);
                    return chatStompHandler.handle(message, channel);
                } else if (destination.contains("walk")) {
                    log.debug("Walk 메시지 라우팅: {}", destination);
                    return walkStompHandler.handle(message, channel);
                }
            }

            // ✅ 인증 정보가 있으면 SecurityContext에 반영 (SEND/SUBSCRIBE 시)
            if (accessor.getUser() instanceof Authentication auth) {
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생", e);
            // 오류가 발생해도 메시지 처리는 계속 진행
        }

        return message;
    }
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//        StompCommand command = accessor.getCommand();
//
//        // ✅ CONNECT 시만 WalkStompHandler로 위임
//        if (StompCommand.CONNECT.equals(command)) {
//            return walkStompHandler.handle(message, channel);
//        }
//
//        // ✅ 인증 정보가 있으면 SecurityContext에 반영 (SEND/SUBSCRIBE 시)
//        if (accessor.getUser() instanceof Authentication auth) {
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        return message;
//    }
}