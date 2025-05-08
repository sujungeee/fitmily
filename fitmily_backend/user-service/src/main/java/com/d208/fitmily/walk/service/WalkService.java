package com.d208.fitmily.walk.service;

import com.d208.fitmily.health.dto.HealthResponseDto;
import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.health.service.HealthService;
import com.d208.fitmily.user.entity.User;
import com.d208.fitmily.user.service.UserService;
import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.dto.WalkResponseDto;
import com.d208.fitmily.walk.entity.Walk;
import com.d208.fitmily.walk.mapper.WalkMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.Duration.between;


@Service
@RequiredArgsConstructor
public class WalkService {

    private final WalkMapper walkMapper;
    private final UserService userService;
    private final HealthService healthService;

    // 산책 중지 (칼로리 계산에서 막힘 일단 패스 )
    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        User user = userService.getUserById(userId);
        HealthResponseDto health = healthService.getLatestHealth(userId);

        float weight = health.getHealthWeight();
        float hr = dto.getWalkHeartRate();
        long walkTime = between(dto.getWalkStartTime(), dto.getWalkEndTime()).toMinutes();

        Walk walk = Walk.builder()
                .userId(userId)
                .walkRouteImg(dto.getWalkRouteImg())
                .walkStartTime(dto.getWalkStartTime())
                .walkEndTime(dto.getWalkEndTime())
                .walkDistance(dto.getWalkDistance())
                .walkHeartRate(dto.getWalkHeartRate())
                .build();
        walkMapper.insertStopWalk(walk);
    }

    // 산책 조회
    public List<WalkResponseDto> findWalks(Integer userId, LocalDateTime start, LocalDateTime end) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("start", start);
        params.put("end", end);

        List<Walk> walks = walkMapper.selectWalks(params);

        User user = userService.getUserById(userId);

        List<WalkResponseDto> dtos = new ArrayList<>();

        for (Walk w : walks) {
            WalkResponseDto dto = WalkResponseDto.builder()
                    .walkId(w.getWalkId())
                    .userId(w.getUserId())
                    .walkRouteImg(w.getWalkRouteImg())
                    .walkStartTime(w.getWalkStartTime())
                    .walkEndTime(w.getWalkEndTime())
                    .walkDistance(w.getWalkDistance())
                    .walkHeartRate(w.getWalkHeartRate())
                    .walkCalories(w.getWalkCalories())
                    .userNickname(user.getNickname())
                    .userProfileImg(user.getProfileImg())
                    // 평균 페이스 가능 추가 가능?
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }
}



