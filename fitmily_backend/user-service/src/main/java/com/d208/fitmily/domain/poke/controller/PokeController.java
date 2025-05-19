package com.d208.fitmily.domain.poke.controller;

import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.poke.service.PokeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/poke")
@RequiredArgsConstructor
@Tag(name = "Poke", description = "콕 찌르기 관련 API")
public class PokeController {

    private final PokeService pokeService;

    @Operation(summary = "콕 찌르기 전송", description = "대상 사용자에게 콕 찌르기를 전송합니다.")
    @PostMapping("/{targetUserId}")
    public ResponseEntity<Map<String, Object>> sendPoke(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable int targetUserId) {

        int senderId = principal.getId();
        log.info("콕 찌르기 요청: senderId={}, targetId={}", senderId, targetUserId);

        LocalDateTime sentAt = pokeService.sendPoke(senderId, targetUserId);

        // 응답 생성
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        data.put("sentAt", sentAt.format(DateTimeFormatter.ISO_DATE_TIME));
        response.put("data", data);

        // response를 실제로 반환
        return ResponseEntity.ok(response);
    }
}