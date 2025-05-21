package com.d208.fitmily.domain.exercise.controller;

import com.d208.fitmily.domain.exercise.dto.*;
import com.d208.fitmily.domain.exercise.service.ExerciseGoalService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.global.config.SecurityConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Tag(name = "운동목표 API", description = "운동 목표 생성/조회/수정/삭제")
@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class ExerciseGoalController {

    private final ExerciseGoalService exerciseGoalService;

    /**
     * 개인운동 목표 조회
     * @param principal 인증된 사용자 정보
     * @param date 조회할 날짜 (기본값: 오늘)
     * @return 운동 목표 응답
     */
    @Operation(summary = "개인운동 목표 조회")
    @GetMapping
    public ResponseEntity<ExerciseGoalResponse> getGoals(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam(required = false) String date) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getId();
        ExerciseGoalResponse response;

        // 날짜가 없으면 오늘 날짜를 기본값으로 사용
        if (date == null || date.isEmpty()) {
            // 오늘 날짜를 yyyy-MM-dd 형식으로 포맷팅
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String today = sdf.format(new Date());
            response = exerciseGoalService.getGoalsByDate(userId, today);
        } else {
            response = exerciseGoalService.getGoalsByDate(userId, date);
        }

        return ResponseEntity.ok(response);
    }

    /**
     * 운동 목표 등록
     */
    @Operation(summary = "운동 목표 등록")
    @PostMapping
    public ResponseEntity<Void> createGoal(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestBody ExerciseGoalRequest request) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getId();

        // ExerciseGoalRequest를 ExerciseGoalDto로 변환 - 여기 변경 필요
        ExerciseGoalDto goalDto = new ExerciseGoalDto();
        goalDto.setExerciseGoalName(request.getExerciseGoalName());
        goalDto.setExerciseGoalValue(request.getExerciseGoalValue());

        exerciseGoalService.createGoal(userId, goalDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 운동 목표 수정
     */
    @Operation(summary = "운동 목표 수정")
    @PatchMapping("/{goalId}")
    public ResponseEntity<Void> updateGoal(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("goalId") Integer goalId,
            @RequestBody ExerciseGoalUpdateRequest request) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getId();
        exerciseGoalService.updateGoal(userId, goalId, request.getExerciseGoalValue());
        return ResponseEntity.ok().build();
    }

    /**
     * 운동 목표 삭제
     * @param principal 인증된 사용자 정보
     * @param goalId 목표 ID
     * @return 응답 상태
     */
    @Operation(summary = "운동 목표 삭제")
    @DeleteMapping("/{goalId}")
    public ResponseEntity<Void> deleteGoal(
            @AuthenticationPrincipal CustomUserDetails principal,
            @PathVariable("goalId") Integer goalId) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getId();
        exerciseGoalService.deleteGoal(userId, goalId);
        return ResponseEntity.ok().build();
    }


    //
     //
    @GetMapping("/weekly-progress")
    public ResponseEntity<WeeklyGoalProgressResponse> getWeeklyGoalProgress() {
        int userId = SecurityConfig.getCurrentUserId();
        WeeklyGoalProgressResponse response = exerciseGoalService.getWeeklyGoalProgress(userId);
        return ResponseEntity.ok(response);
    }
    
}