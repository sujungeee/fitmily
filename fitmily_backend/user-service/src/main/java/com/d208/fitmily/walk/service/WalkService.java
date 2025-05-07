package com.d208.fitmily.walk.service;

import com.d208.fitmily.health.dto.HealthResponseDto;
import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.health.service.HealthService;
import com.d208.fitmily.user.entity.User;
import com.d208.fitmily.user.service.UserService;
import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.entity.Walk;
import com.d208.fitmily.walk.mapper.WalkMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

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

}



