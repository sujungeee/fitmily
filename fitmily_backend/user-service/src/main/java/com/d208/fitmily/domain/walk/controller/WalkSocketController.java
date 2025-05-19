package com.d208.fitmily.domain.walk.controller;

import com.d208.fitmily.domain.walk.dto.GpsDto;
import com.d208.fitmily.domain.walk.service.WalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
@RequiredArgsConstructor
@Controller
public class WalkSocketController {

    private final WalkService walkService;

    @MessageMapping("/walk/gps")
    public void handleGps(@Payload GpsDto gpsDto, Message<?> message) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal principal = accessor.getUser();

        System.out.println("✅ 메시지에서 추출gpsDto한 Principal = " + principal);
        System.out.println("✅ = " + gpsDto.getLat());
        System.out.println("messager = " + message.getHeaders());
        System.out.println("messager = " + message.getPayload());
        System.out.println("messager = " + message.getClass());
        System.out.println("gpsDto = " + gpsDto.getUserId());

        Integer userId = gpsDto.getUserId();

        walkService.processGps(userId, gpsDto);

    }
}
