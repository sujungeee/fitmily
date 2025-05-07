package com.d208.fitmily.health.controller;


import com.d208.fitmily.common.response.ApiResponse;
import com.d208.fitmily.health.dto.AddHealthRequestDto;
import com.d208.fitmily.health.service.HealthService;
import com.d208.fitmily.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HealthController {

    private final HealthService healthService;


    @Operation(summary = "건강 상태 등록", description = "JWT에서 추출한 userId로 건강 상태를 저장합니다.")
    @PostMapping
    public ApiResponse<Void> addHealth(
            @ResponseBody AddHealthRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails principal
            ){
        healthService.addHealth(principal.getId(),dto);
        return ApiResponse.ok(null, "건강 상태가 등록되었습니다");
    }



    //건강상태 조회

    //건강상태 수정

}
