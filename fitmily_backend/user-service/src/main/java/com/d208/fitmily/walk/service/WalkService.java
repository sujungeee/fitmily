package com.d208.fitmily.walk.service;

import com.d208.fitmily.health.dto.HealthResponseDto;
//import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.health.service.HealthService;
import com.d208.fitmily.user.entity.User;
import com.d208.fitmily.user.service.UserService;
import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.dto.UserDto;
import com.d208.fitmily.walk.dto.WalkResponseDto;
import com.d208.fitmily.walk.entity.Walk;
import com.d208.fitmily.walk.mapper.WalkMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final StringRedisTemplate redisTemplate;


    // 산책 중지 (칼로리 계산에서 막힘 일단 패스 )
    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        User user = userService.getUserById(userId);
        HealthResponseDto health = healthService.getLatestHealth(userId);

        float weight = health.getWeight();
        long walkTime = between(dto.getStartTime(), dto.getEndTime()).toMinutes();

        Walk walk = Walk.builder()
                .userId(userId)
                .routeImg(dto.getRouteImg())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .distance(dto.getDistance())
                .build();
        walkMapper.insertStopWalk(walk);
    }

    // 산책 기록 조회
    public List<WalkResponseDto> findWalks(Integer userId, LocalDateTime start, LocalDateTime end) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("start",   start);
        params.put("end",     end);

        return walkMapper.selectWalks(params);
    }

    // 산책 목표 여부 조회
    public Boolean walkGoalExists(Integer userId){
        return walkMapper.walkGoalExists(userId);
    }

    // 산책중인 가족 구성원 조회
//    public List<UserDto> getWalkingFamilyMembers(Integer familyId) {
//
//        // familyId로 가족 구성원의 userId 다 리스트로 가져옴
//        List<Integer> userIds = familyService.getUserIdsByFamilyId(familyId);
//        List<UserDto> result = new ArrayList<>();
//
//        for (Integer userId : userIds) {
//            if (redisTemplate.hasKey("walk:gps:" + userId)) {
//                result.add(UserDto.builder()
//                        .userId(user.getUserId())
////                        .name(user.getName())
////                        .profileImg(user.getProfileImg())
//                        .build());
//            }
//        }
//        return result;
//            }
//        }
}



