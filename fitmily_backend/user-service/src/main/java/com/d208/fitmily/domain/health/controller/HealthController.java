package com.d208.fitmily.domain.health.controller;

import com.d208.fitmily.domain.health.dto.AddHealthRequestDto;
import com.d208.fitmily.domain.health.dto.HealthResponseDto;
import com.d208.fitmily.domain.health.dto.UpdateHealthRequestDto;
import com.d208.fitmily.domain.health.service.HealthService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "건강상태 API", description = "건강상태 추가,조회,수정")
public class HealthController {

    private final HealthService healthService;


    @Operation(summary = "건강 상태 등록", description = "- JWT에서 추출한 userId로 건강 상태를 저장합니다.")
    @PostMapping("health") //주소 아직 안정함
    public ResponseEntity<Void> addHealth(@RequestBody AddHealthRequestDto dto, @AuthenticationPrincipal CustomUserDetails principal) throws JsonProcessingException {
        healthService.addHealth(principal.getId(),dto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "건강 상태 조회", description = "- JWT에서 추출한 userId로 건강 상태를 조회합니다.")
    @GetMapping("health")
    public ResponseEntity<HealthResponseDto> gethealth(@AuthenticationPrincipal CustomUserDetails principal){
        HealthResponseDto dto = healthService.getLatestHealth(principal.getId());
        return ResponseEntity.ok(dto);
    }


    @Operation(summary = "건강 상태 수정", description = "- 건강상태를 수정합니다")
    @PatchMapping("health")
    public ResponseEntity<Void> updateHealth(
            @RequestBody UpdateHealthRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails principal) throws JsonProcessingException {

        healthService.updateHealth(principal.getId(), dto);
        return ResponseEntity.ok(null);
    }


}
