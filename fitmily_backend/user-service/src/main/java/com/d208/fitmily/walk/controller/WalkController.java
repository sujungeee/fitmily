package com.d208.fitmily.walk.controller;


import com.d208.fitmily.common.response.ApiResponse;
import com.d208.fitmily.user.dto.CustomUserDetails;
import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api")
public class WalkController {

    private final WalkService walkService;

    @Operation(summary = "산책 종료", description = "산책 중지 시점 데이터를 저장합니다. ")
    @PostMapping("/walks/end")
    public ApiResponse<Void> endWalk(@RequestBody EndWalkRequestDto dto,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        walkService.endWalk(principal.getId(),dto);
        return ApiResponse.ok(null,"산책이 종료됌");
    }
}
    //산책기록 조회

    //산책 기록 상세 조회

    //산책 목표 존재 여부 조회



