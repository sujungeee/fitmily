package com.d208.fitmily.global.handler;

import com.d208.fitmily.global.handler.WalkStompHandler;
import lombok.RequiredArgsConstructor;
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