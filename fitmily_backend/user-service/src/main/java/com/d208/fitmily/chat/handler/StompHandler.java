package com.d208.fitmily.chat.handler;

import com.d208.fitmily.user.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket 연결 시 사용자 온라인 상태 처리
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            if (accessor.getUser() instanceof Authentication) {
                Authentication auth = (Authentication) accessor.getUser();
                if (auth.getPrincipal() instanceof CustomUserDetails) {
                    Integer userId = ((CustomUserDetails) auth.getPrincipal()).getId();

                    // Redis에 온라인 상태 저장
                    try {
                        // 사용자 온라인 상태를 현재 타임스탬프로 저장
                        redisTemplate.opsForValue().set(
                                "user:online:" + userId,
                                String.valueOf(System.currentTimeMillis()),
                                1, // 1시간 TTL 설정
                                TimeUnit.HOURS
                        );
                    } catch (Exception e) {
                        System.out.println("Redis 온라인 상태 저장 실패: " + e.getMessage());
                    }
                }
            }
        }

        // 채팅방 구독 처리
        else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String destination = accessor.getDestination();
            if (destination != null && destination.startsWith("/topic/chat/family/")) {
                String familyId = destination.replace("/topic/chat/family/", "")
                        .split("/")[0]; // 추가 경로가 있을 경우 제거

                if (accessor.getUser() instanceof Authentication) {
                    Authentication auth = (Authentication) accessor.getUser();
                    if (auth.getPrincipal() instanceof CustomUserDetails) {
                        Integer userId = ((CustomUserDetails) auth.getPrincipal()).getId();

                        // Redis에 구독 정보 저장
                        try {
                            // 가족 채팅방 구독자 목록에 사용자 추가
                            redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", userId.toString());

                            // 미확인 메시지 개수 확인 및 초기화 (채팅방에 접속 시 초기화 필요할 경우)
                            String lastReadMsgId = (String) redisTemplate.opsForValue().get("read:" + familyId + ":" + userId);
                            if (lastReadMsgId == null) {
                                // 처음 구독하는 경우 마지막 읽은 메시지 ID를 0으로 초기화
                                redisTemplate.opsForValue().set("read:" + familyId + ":" + userId, "0");
                            }
                        } catch (Exception e) {
                            System.out.println("Redis 구독 정보 저장 실패: " + e.getMessage());
                        }
                    }
                }
            }
        }

        // 연결 종료 시
        else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            if (accessor.getUser() instanceof Authentication) {
                Authentication auth = (Authentication) accessor.getUser();
                if (auth.getPrincipal() instanceof CustomUserDetails) {
                    Integer userId = ((CustomUserDetails) auth.getPrincipal()).getId();

                    // Redis에서 온라인 상태 제거
                    try {
                        redisTemplate.delete("user:online:" + userId);

                    } catch (Exception e) {
                        System.out.println("Redis 연결 종료 처리 실패: " + e.getMessage());
                    }
                }
            }
        }

        return message;
    }
}