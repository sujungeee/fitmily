package com.d208.fitmily.health.controller;


import com.d208.fitmily.common.response.ApiResponse;
import com.d208.fitmily.health.dto.AddHealthRequestDto;
import com.d208.fitmily.health.dto.HealthResponseDto;
import com.d208.fitmily.health.service.HealthService;
import com.d208.fitmily.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "건강상태 API", description = "건강상태 추가,조회,수정")
public class HealthController {

    private final HealthService healthService;


    @Operation(summary = "건강 상태 등록", description = "JWT에서 추출한 userId로 건강 상태를 저장합니다.")
    @PostMapping("health") //주소 아직 안정함
    public ApiResponse<Void> addHealth(@RequestBody AddHealthRequestDto dto, @AuthenticationPrincipal CustomUserDetails principal){
        healthService.addHealth(principal.getId(),dto);
        return ApiResponse.ok(null, "건강 상태가 등록되었습니다");
    }
    //건강상태 조회

    @Operation(summary = "건강 상태 조회", description = "JWT에서 추출한 userId로 건강 상태를 조회합니다.")
    @GetMapping("health")
    public ApiResponse<HealthResponseDto> gethealth(@AuthenticationPrincipal CustomUserDetails principal){
        HealthResponseDto dto = healthService.getLatestHealth(principal.getId());
        return ApiResponse.ok(dto, "최신 건강 상태 조회 성공");
    }


    //건강상태 수정

}
