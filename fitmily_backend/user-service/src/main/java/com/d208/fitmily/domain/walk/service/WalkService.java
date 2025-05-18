package com.d208.fitmily.domain.walk.service;

import com.d208.fitmily.domain.walkchallenge.service.WalkChallengeService;
import com.d208.fitmily.domain.health.dto.HealthResponseDto;
//import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.domain.health.service.HealthService;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.user.service.UserService;
import com.d208.fitmily.domain.walk.dto.*;
import com.d208.fitmily.domain.walk.mapper.WalkMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.Duration.between;


@Service
@RequiredArgsConstructor
public class WalkService {

    private final WalkMapper walkMapper;
    private final UserService userService;
    private final HealthService healthService;
    private final StringRedisTemplate redisTemplate;
    private final GpsRedisService gpsRedisService;
    private final SimpMessagingTemplate messagingTemplate;
    private final SseService sseService;
    private final WalkChallengeService walkChallengeService;


    // 산책 중지 (칼로리 계산에서 막힘 일단 패스 )
    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        User user = userService.getUserById(userId);
        HealthResponseDto health = healthService.getLatestHealth(userId);

        float weight = health.getWeight();
        Instant startTime = dto.getStartTime().toInstant();
        Instant endTime = dto.getEndTime().toInstant();

        long walkingTime = Duration.between(startTime, endTime).toMinutes();

        // 칼로리계산식 = MET × 체중(kg) × 운동시간(hr)
        // MET = 운동강도 (3.5가 평균)
        final double MET = 3.5;
        double walkHours = walkingTime / 60.0;
        double walkCalories = MET * weight * walkHours;

        StopWalkDto stopWalkDto = StopWalkDto.builder()
                .userId(userId)
                .routeImg(dto.getRouteImg())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .distance(dto.getDistance())
                .calories((float) walkCalories)
                .build();

        walkMapper.insertStopWalk(stopWalkDto);

        // 산책 챌린지 거리 업데이트
        walkChallengeService.updateChallengeDistance(stopWalkDto);
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

    // 산책 시작했을때
    public void processGps(Integer userId, GpsDto gpsDto){

        boolean isFirst = !redisTemplate.hasKey("walk:gps:" + userId); //키가 없으면, 산책 시작
        gpsRedisService.saveGps(userId, gpsDto); //gps redis에 저장

        if (isFirst){
            UserDto user = userService.getUserDtoById(userId);

            Integer familyId = user.getFamilyId();

            WalkStartDto data = WalkStartDto.builder()
                    .userId(user.getUserId())
                    .userNickname(user.getUserNickname())
                    .userZodiacName(user.getUserZodiacName())
                    .build();

            //sse 전송
            sseService.sendFamilyWalkingEvent(familyId, data);
        }

        // 데이터 전송
        String topic = "/topic/walk/gps/" + userId;
        messagingTemplate.convertAndSend(topic, gpsDto);
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



