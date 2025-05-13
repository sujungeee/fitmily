package com.d208.fitmily.walk.controller;


import com.d208.fitmily.common.response.ApiResponse;
import com.d208.fitmily.user.dto.CustomUserDetails;
import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.dto.GpsDto;
import com.d208.fitmily.walk.dto.WalkResponseDto;
import com.d208.fitmily.walk.service.GpsRedisService;
import com.d208.fitmily.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api")
@Tag(name = "산책 API", description = "산책관련 API")
public class WalkController {

    private final WalkService walkService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GpsRedisService gpsRedisService;

    //산책 시작
    @MessageMapping("/walk/gps")  // /app/walk/gps 로 전송된 메시지를 처리함
    public void handleGps(@Payload GpsDto gpsDto) {
        gpsRedisService.saveGps(gpsDto);

        String topic = "/topic/walk/gps/" + gpsDto.getUserId();
        messagingTemplate.convertAndSend(topic, gpsDto); //브로드캐스팅 역할
        // 다음 단계에서 Redis 저장 추가
    }

    @GetMapping("/gps/{userId}")
    public ApiResponse<List<GpsDto>> getGpsList(@PathVariable Integer userId) {
        List<GpsDto> gpsList = gpsRedisService.getGpsListByUserId(userId);
        return ApiResponse.ok(gpsList,"산책 gps데이터 조회완료");
    }


    @Operation(summary = "산책 종료", description = "산책 중지 시점 데이터를 저장합니다. ")
    @PostMapping("/walks/end")
    public ApiResponse<Void> endWalk(@RequestBody EndWalkRequestDto dto,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        walkService.endWalk(principal.getId(),dto);
        return ApiResponse.ok(null,"산책이 종료됌");
    }


    @Operation(summary = "산책 기록 조회", description = "산책 기록을 조회합니다. ")
    @GetMapping("/walks")
    public ApiResponse<List<WalkResponseDto>> getWalks(
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime end
    ){
        List<WalkResponseDto> list = walkService.findWalks(userId, start, end);
        return ApiResponse.ok(list, "산책 기록 조회 성공");
    }

    //산책 목표 존재 여부 조회
    @Operation(summary = "산책 목표 조회", description = "- 목표 존재하면 = true  \n- 목표존재하지 않으면 = false")
    @GetMapping("/walks/goal/exist")
    public ApiResponse<Boolean> goalexist(@AuthenticationPrincipal CustomUserDetails principal){
        boolean Existence = walkService.walkGoalExists(principal.getId());
        return ApiResponse.ok(Existence, "산책 기록 조회 성공");
    }



}

