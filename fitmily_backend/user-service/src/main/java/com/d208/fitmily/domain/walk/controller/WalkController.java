package com.d208.fitmily.domain.walk.controller;



import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.domain.walk.dto.GpsDto;
import com.d208.fitmily.domain.walk.dto.UserDto;
import com.d208.fitmily.domain.walk.dto.WalkResponseDto;
import com.d208.fitmily.domain.walk.service.GpsRedisService;
import com.d208.fitmily.domain.walk.service.SseService;
import com.d208.fitmily.domain.walk.service.WalkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api")
@Tag(name = "산책 API", description = "산책관련 API")
public class WalkController {

    private final WalkService walkService;

    private final GpsRedisService gpsRedisService;
    private final SseService sseService;


    @Operation(summary = "산책중 gps 데이터 조회 ", description = "산책중인 사용자의 이전 gps 데이터를 전부 조회합니다. ")
    @GetMapping("/walks/gps/{userId}")
    public ResponseEntity<Map<String, Object>> getGpsList(@PathVariable Integer userId) {
        List<GpsDto> gpsList = gpsRedisService.getGpsListByUserId(userId);
        return ResponseEntity.ok(Map.of("path", gpsList));
    }


    @Operation(summary = "산책 종료", description = "산책 중지 시점 데이터를 저장합니다. ")
    @PostMapping("/walks/end")
    public ResponseEntity<Void> endWalk(@RequestBody EndWalkRequestDto dto,
                                     @AuthenticationPrincipal CustomUserDetails principal) {
        walkService.endWalk(principal.getId(),dto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "산책 기록 조회", description = "산책 기록을 조회합니다.")
    @GetMapping("/walks")
    public ResponseEntity<Map<String, Object>> getWalks(@AuthenticationPrincipal CustomUserDetails principal) {
        System.out.println("산책기록조회 컨트롤러 들어옴");
        Integer userId = principal.getId();
        List<WalkResponseDto> walks = walkService.findFamilyWalks(userId);
        return ResponseEntity.ok(Map.of("walk", walks));
    }

    @Operation(summary = "산책 목표 조회", description = "- 목표 존재하면 = true  \n- 목표존재하지 않으면 = false")
    @GetMapping("/walks/goal/exist")
    public ResponseEntity<Boolean> goalexist(@AuthenticationPrincipal CustomUserDetails principal){
        boolean Existence = walkService.walkGoalExists(principal.getId());
        return ResponseEntity.ok(Existence);
    }

    @Operation(summary = "산책 SSE 연결 ")
    @GetMapping(value = "families/{familyId}/walks/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getWalkSubscribe(@PathVariable Integer familyId) {
        return sseService.connectFamilyEmitter(familyId);
    }

    @Operation(summary = "산책중인 가족 리스트 조회 ", description = "산책중인 가족들의 리스트를 조회합니다. ")
    @GetMapping("family/walking-members")
    public ResponseEntity<Map<String, Object>> getWalkingFamilyMembers(@RequestParam Integer familyId) {
        List<UserDto> walkingUsers = walkService.getWalkingFamilyMembers(familyId);
        return ResponseEntity.ok(Map.of("member", walkingUsers));
    }
}

