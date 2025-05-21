package com.d208.fitmily.domain.poke.controller;

import com.d208.fitmily.domain.poke.dto.PokeRequest;
import com.d208.fitmily.domain.poke.service.PokeService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/poke")
@RequiredArgsConstructor
@Tag(name = "Poke", description = "콕 찌르기 관련 API")
public class PokeController {

    private final PokeService pokeService;

    @Operation(summary = "콕 찌르기 전송", description = "대상 사용자에게 콕 찌르기를 전송합니다.")
    @PostMapping("/{targetUserId}")
    public ResponseEntity<Object> sendPoke(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable int targetUserId) {

        // 인증된 사용자 정보 확인
        if (principal == null) {
            log.error("인증 정보가 없습니다. 콕 찌르기 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int senderId = principal.getId();
        log.info("콕 찌르기 요청: senderId={}, targetId={}", senderId, targetUserId);

        pokeService.sendPoke(senderId, targetUserId);
        return ResponseEntity.ok().build();
    }
}