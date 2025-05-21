package com.d208.fitmily.domain.walk.service;

import com.d208.fitmily.domain.AwsS3.Service.AwsS3Service;
import com.d208.fitmily.domain.fcm.service.FcmService;
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
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.Duration.between;
@Service
@RequiredArgsConstructor
@Slf4j
public class WalkService {

    //    private final SseService sseService;
    private final WalkMapper walkMapper;
    private final UserService userService;
    private final HealthService healthService;
    private final StringRedisTemplate redisTemplate;
    private final GpsRedisService gpsRedisService;
    private final SimpMessagingTemplate messagingTemplate;
    private final WalkChallengeService walkChallengeService;
    private final AwsS3Service awsS3Service;
    private final FcmService fcmService;


    // 산책 중지
    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        UserDto userDto = userService.getUserDtoById(userId);
        HealthResponseDto health = healthService.getLatestHealth(userId);

        float weight = Optional.ofNullable(health.getWeight()).orElse(66.0f);
        Instant startTime = dto.getWalkStartTime().toInstant();
        Instant endTime = dto.getWalkEndTime().toInstant();

        long walkingTime = Duration.between(startTime, endTime).toMinutes();

        // 칼로리계산식 = MET × 체중(kg) × 운동시간(hr)
        // MET = 운동강도 (3.5가 평균)
        final float  MET = 3.5f;
        float walkHours = walkingTime / 60.0f;
        float floatWalkCalories = MET * weight * walkHours;

        int walkCalories = Math.round(floatWalkCalories);

        StopWalkDto stopWalkDto = StopWalkDto.builder()
                .userId(userId)
                .walkRouteImg(dto.getWalkRouteImg())
                .walkStartTime(dto.getWalkStartTime())
                .walkEndTime(dto.getWalkEndTime())
                .walkDistance(dto.getWalkDistance())
                .calories(walkCalories)
                .build();

        walkMapper.insertStopWalk(stopWalkDto);

//        // 산책 챌린지 거리 업데이트
//        walkChallengeService.updateChallengeDistance(stopWalkDto);

//        // FCM 알림 전송 (산책 종료)
//        if (userDto.getFamilyId() != null) {
//            try {
//                fcmService.sendWalkEndNotification(
//                        userDto,
//                        userDto.getFamilyId(),
//                        dto.getWalkDistance(),
//                        walkCalories,
//                        walkingTime
//                );
//            } catch (Exception e) {
//                log.error("산책 종료 알림 전송 실패: {}", e.getMessage());
//                // 알림 전송 실패해도 정상 처리
//            }
//        }
        //gps 데이터 삭제
        gpsRedisService.removeWalkData(userId);

    }




    // 산책 기록 조회
    public List<WalkResponseDto> findWalks(Integer userId, LocalDateTime start, LocalDateTime end) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("start",   start);
        params.put("end",     end);

        List<WalkResponseDto> walks = walkMapper.selectWalks(params);

        for(WalkResponseDto walk : walks){
            String routeImg = walk.getRouteImg();
            System.out.println("routeImg: " + routeImg);
            if (routeImg != null && !routeImg.isBlank()){
                String presignedUrl = awsS3Service.generatePresignedDownloadUrl(routeImg);
                System.out.println("presignedUrl" + presignedUrl);
                walk.setRouteImg(presignedUrl);
            }
        }
        return walks;
    }



    // 산책 목표 여부 조회
    public Boolean walkGoalExists(Integer userId){
        return walkMapper.walkGoalExists(userId);
    }




    // 산책 시작했을때
    public void processGps(Integer userId, GpsDto gpsDto){
        System.out.println("서비스 들어옴");
        boolean isFirst = !redisTemplate.hasKey("walk:gps:" + userId); //키가 없으면, 산책 시작
        gpsRedisService.saveGps(userId, gpsDto); //gps redis에 저장

//        if (isFirst){
//            UserDto user = userService.getUserDtoById(userId);
//
//            Integer familyId = user.getFamilyId();
//
//            WalkStartDto data = WalkStartDto.builder()
//                    .userId(user.getUserId())
//                    .userNickname(user.getUserNickname())
//                    .userZodiacName(user.getUserZodiacName())
//                    .build();
//
//            //sse 전송
////            sseService.sendFamilyWalkingEvent(familyId, data);
//        }

        // 처음 GPS 데이터가 들어왔을 때 (산책 시작)
        if (isFirst){
            UserDto user = userService.getUserDtoById(userId);

            // FCM 알림 전송 (산책 시작)
            if (user.getFamilyId() != null) {
                try {
                    fcmService.sendWalkStartNotification(user, user.getFamilyId());
                } catch (Exception e) {
                    log.error("산책 시작 알림 전송 실패: {}", e.getMessage());
                    // 알림 전송 실패해도 정상 처리
                }
            }
        }

        // 데이터 전송
        String topic = "/topic/walk/gps/" + userId;
        messagingTemplate.convertAndSend(topic, gpsDto);
        System.out.println("topic"+ gpsDto.getLat());
        System.out.println("데이터 발송완료");
    }


    // 산책중인 가족 구성원 조회
    public List<UserDto> getWalkingFamilyMembers(Integer familyId) {

        // familyId로 가족 구성원의 userId 다 리스트로 가져와서
        List<Integer> userIds = userService.getUserIdsByFamilyId(familyId);
        System.out.println("패밀리 구성원 : " + userIds);

        // userId가 없다면 return
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }

        // userId를 돌면서 walk:gps:userId가 있는지 = 산책중인지 확인
        List<Integer> walkingUserIds = new ArrayList<>();
        for (Integer userId : userIds) {
            if (redisTemplate.hasKey("walk:gps:" + userId)) {
                walkingUserIds.add(userId);
            }
        }
        System.out.println("산책한 구성원 : " + walkingUserIds);

        // 운동중인 사용자 배열이 비었다면 return
        if (walkingUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        //산책중인 userId로 이름,가족가입순서, 띠 정보 가져옴
        List<User> walkingUsers = userService.getUsersByIds(walkingUserIds);

        List<UserDto> result = new ArrayList<>();
        for (User user : walkingUsers) {
            System.out.println("산책중인 사람들" + user.getUserId()+ user.getFamilySequence()+ user.getNickname()+user.getFamilyId());
            if (user == null) continue;
            result.add(UserDto.builder()
                    .userId(user.getUserId())
                    .userNickname(user.getUserNickname())
                    .userFamilySequence(user.getUserFamilySequence())
                    .userZodiacName(user.getUserZodiacName())
                    .familyId(user.getFamilyId())
                    .build());
        }

        return result;
    }
}