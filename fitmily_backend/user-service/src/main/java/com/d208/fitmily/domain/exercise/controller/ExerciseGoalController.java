package com.d208.fitmily.domain.exercise.controller;

import com.d208.fitmily.domain.exercise.dto.ExerciseGoalDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalRequest;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalResponse;
import com.d208.fitmily.domain.exercise.dto.ExerciseGoalUpdateRequest;
import com.d208.fitmily.domain.exercise.service.ExerciseGoalService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "운동목표 API", description = "운동 목표 생성/조회/수정/삭제")
@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class ExerciseGoalController {

    private final ExerciseGoalService exerciseGoalService;

    /**
     * 개인운동 목표 조회
     * @param principal 인증된 사용자 정보
     * @return 운동 목표 응답
     */
    @Operation(summary = "개인운동 목표 조회", description = "사용자의 모든 운동 목표를 조회합니다.")
    @GetMapping
    public ResponseEntity<ExerciseGoalResponse> getGoals(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer userId = principal.getId();
        ExerciseGoalResponse response = exerciseGoalService.getGoals(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 운동 목표 등록
     */
    @Operation(summary = "운동 목표 등록", description = "새로운 운동 목표를 등록합니다.")
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
    @Operation(summary = "운동 목표 수정", description = "기존 운동 목표의 목표값을 수정합니다.")
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
    @Operation(summary = "운동 목표 삭제", description = "운동 목표를 삭제합니다.")
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
}