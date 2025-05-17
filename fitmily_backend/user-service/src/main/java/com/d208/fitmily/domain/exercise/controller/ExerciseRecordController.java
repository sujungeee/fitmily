package com.d208.fitmily.domain.exercise.controller;


import com.d208.fitmily.domain.exercise.dto.ExerciseRecordRequestDto;
import com.d208.fitmily.domain.exercise.dto.ExerciseRecordResponseDto;
import com.d208.fitmily.domain.exercise.service.ExerciseRecordService;
import com.d208.fitmily.domain.user.dto.CustomUserDetails;
import com.d208.fitmily.domain.walk.dto.GpsDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "운동기록 API", description = "운동 기록 추가, 조회")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor

public class ExerciseRecordController {

    private final ExerciseRecordService exerciseService;;


    @Operation(summary = "개인 운동 기록")
    @PostMapping("/exercise")
    public ResponseEntity<Void> ExerciseRecord(@RequestBody ExerciseRecordRequestDto dto,
                                               @AuthenticationPrincipal CustomUserDetails principal){
        Integer userId = principal.getId();
        exerciseService.recordExercise(userId, dto);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "개인 운동 기록 조회 (일반운동 + 산책)")
    @GetMapping("/exercise")
    public ResponseEntity<Map<String, Object>> getExerciseRecords(@AuthenticationPrincipal CustomUserDetails principal) {
        Integer userId = principal.getId();
        List<ExerciseRecordResponseDto> records = exerciseService.getDailyExerciseRecords(userId);
        return ResponseEntity.ok(Map.of("exercise", records));
    }



}
