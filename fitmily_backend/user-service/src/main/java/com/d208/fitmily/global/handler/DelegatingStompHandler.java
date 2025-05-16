package com.d208.fitmily.global.handler;

import com.d208.fitmily.global.handler.WalkStompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DelegatingStompHandler implements ChannelInterceptor {

    //    private final ChatStompHandler chatStompHandler;
    private final WalkStompHandler walkStompHandler;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();
        String dest = accessor.getDestination();

        // ✅ CONNECT 메시지는 무조건 walk로 넘기도록
        if (StompCommand.CONNECT.equals(command)) {
            return walkStompHandler.handle(message, channel);
        }

//        if (dest != null && dest.startsWith("/topic/chat")) {
//            return chatStompHandler.handle(message, channel);
//        }
        if (dest != null && dest.startsWith("/app/walk")) { //topic/walk로 들어오면 이 핸들러 실행됨
            return walkStompHandler.handle(message, channel);
        }
        return message;
    }
}