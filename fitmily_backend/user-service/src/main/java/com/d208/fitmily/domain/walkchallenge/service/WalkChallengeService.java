package com.d208.fitmily.domain.walkchallenge.service;

import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.domain.walk.dto.UserDto;
import com.d208.fitmily.domain.walkchallenge.dto.*;
import com.d208.fitmily.domain.walkchallenge.mapper.WalkChallengeMapper;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.domain.user.service.UserService;
import com.d208.fitmily.domain.walk.dto.StopWalkDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalkChallengeService {

    private final WalkChallengeMapper walkChallengeMapper;
    private final UserService userService;
    private final FamilyMapper familyMapper;
    private static final int DEFAULT_INDIVIDUAL_TARGET_KM = 5;

    /**
     * 매주 월요일 오전 9시에 모든 가족에 대한 챌린지 생성
     */
    @Scheduled(cron = "0 0 9 * * MON")
    @Transactional
    public void createWeeklyChallenges() {
        // 모든 가족 ID 목록 조회
        List<Integer> allFamilyIds = walkChallengeMapper.getAllFamilyIds();

        LocalDate today = LocalDate.now();

        for (Integer familyId : allFamilyIds) {
            createChallengeForFamily(familyId, today);
        }
    }

    /**
     * 특정 가족에 대한 주간 챌린지 생성
     */
    @Transactional
    public void createChallengeForFamily(Integer familyId, LocalDate startDate) {
        // 가족 구성원 수 조회
        int memberCount = familyMapper.findFamilyMembers(familyId).size();

        // 기본 목표 거리 계산 (1인당 5km)
        int baseTargetDistance = memberCount * DEFAULT_INDIVIDUAL_TARGET_KM;
        int targetDistance = baseTargetDistance;

        // 이전 주 챌린지 정보 조회
        WalkChallengeDto previousChallenge = walkChallengeMapper.findPreviousChallenge(familyId, startDate);

        // 이전 챌린지가 있으면 목표 조정
        if (previousChallenge != null) {
            Float totalDistance = walkChallengeMapper.getTotalDistanceByChallenge(previousChallenge.getChallengeId());
            if (totalDistance == null) totalDistance = 0.0f;

            int previousTarget = previousChallenge.getTargetDistance();

            // 달성률 계산
            float achievementRate = (totalDistance / previousTarget) * 100;

            // 목표 조정
            if (achievementRate < 50) {
                // 달성률 50% 미만: 목표 감소 (0.9배)
                targetDistance = Math.round(baseTargetDistance * 0.9f);
            } else if (achievementRate >= 120) {
                // 달성률 120% 이상: 목표 증가 (1.1배)
                targetDistance = Math.round(baseTargetDistance * 1.1f);
            }
        }

        // 항상 정수 값으로 저장
        targetDistance = Math.max(1, targetDistance);

        // 챌린지 생성
        WalkChallengeDto newChallenge = WalkChallengeDto.builder()
                .familyId(familyId)
                .targetDistance(targetDistance)
                .startDate(startDate)
                .build();

        walkChallengeMapper.insertWalkChallenge(newChallenge);

        // 가족 구성원들을 챌린지에 등록
        List<Integer> userIds = familyMapper.findFamilyMembers(familyId)
                .stream()
                .map(user -> user.getUserId())
                .collect(Collectors.toList());

        for (Integer userId : userIds) {
            UserWalkChallengeDto userChallenge = UserWalkChallengeDto.builder()
                    .userId(userId)
                    .challengeId(newChallenge.getChallengeId())
                    .distance(0.0f)  // 초기 거리는 0
                    .build();

            walkChallengeMapper.insertUserWalkChallenge(userChallenge);
        }
    }

    /**
     * 산책 완료 시 챌린지 거리 업데이트
     */
    @Transactional
    public void updateChallengeDistance(StopWalkDto walkDto) {
        Integer userId = walkDto.getUserId();
        Float distance = walkDto.getDistance();

        // 사용자의 가족 ID 조회
        Integer familyId = userService.getUserById(userId).getFamilyId();

        if (familyId == null) {
            return; // 가족이 없는 사용자는 챌린지에 참여할 수 없음
        }

        // 현재 활성화된 챌린지 조회
        WalkChallengeDto activeChallenge = walkChallengeMapper.findActiveChallenge(familyId);

        if (activeChallenge == null) {
            return; // 활성화된 챌린지가 없음
        }

        // 사용자의 챌린지 거리 업데이트
        UserWalkChallengeDto updateDto = UserWalkChallengeDto.builder()
                .userId(userId)
                .challengeId(activeChallenge.getChallengeId())
                .distance(distance)
                .build();

        walkChallengeMapper.updateUserWalkChallengeDistance(updateDto);
    }

    /**
     * 현재 진행 중인 챌린지 조회
     */
    @Transactional
    public void syncChallengeDistances(Integer challengeId) {
        // 챌린지에 참여한 모든 사용자의 거리를 walk 테이블 기준으로 업데이트
        walkChallengeMapper.syncUserWalkChallengeDistances(challengeId);
    }

    // getCurrentChallenge 메서드 내 수정
    public WalkChallengeResponseDto getCurrentChallenge(Integer familyId) {
        // 현재 활성화된 챌린지 조회
        WalkChallengeDto challenge = walkChallengeMapper.findActiveChallenge(familyId);

        if (challenge == null) {
            return null;
        }

        // 모든 산책 기록을 기준으로 챌린지 거리 동기화
        syncChallengeDistances(challenge.getChallengeId());

        // 총 달성 거리 조회
        Float totalDistance = walkChallengeMapper.getTotalDistanceByChallenge(challenge.getChallengeId());
        if (totalDistance == null) totalDistance = 0.0f;

        // 진행 퍼센트 계산
        int progressPercentage = Math.min(100, Math.round((totalDistance / challenge.getTargetDistance()) * 100));

        // 참가자 정보 조회
        List<Map<String, Object>> participantsData = walkChallengeMapper.getChallengeParticipantsWithRank(challenge.getChallengeId());
        List<ParticipantDto> participants;

        // 참가자 데이터가 있으면 사용, 없으면 초기 데이터 생성
        if (participantsData != null && !participantsData.isEmpty()) {
            // 거리가 기록된 참가자 데이터 사용
            participants = participantsData.stream()
                    .map(data -> {
                        // null 값 안전하게 처리
                        Integer userId = (Integer) data.get("user_id");
                        String nickname = (String) data.get("nickname");
                        String profileColor = (String) data.get("profileColor");
                        Float distanceCompleted = (Float) data.get("distanceCompleted");
                        Integer rank = ((Number) data.get("user_rank")).intValue();

                        UserDto userDto = userService.getUserDtoById(userId);
                        return ParticipantDto.builder()
                                .userId(userId)
                                .nickname(userDto.getUserNickname())
                                .familySequence(userDto.getUserFamilySequence())
                                .zodiacName(userDto.getUserZodiacName())
                                .distanceCompleted(distanceCompleted != null ? distanceCompleted : 0.0f)
                                .rank(rank)
                                .build();
                    })
                    .collect(Collectors.toList());
        } else {
            // 챌린지 초기 상태 - DB에 사용자 등록하고 빈 목록 반환
            // 자동으로 모든 가족 구성원 등록
            List<User> familyMembers = familyMapper.findFamilyMembers(familyId);

            if (familyMembers != null && !familyMembers.isEmpty()) {
                log.info("가족 구성원 자동 등록 시작: 가족 ID {}, 챌린지 ID {}", familyId, challenge.getChallengeId());

                // 가족 구성원들을 챌린지에 등록
                for (User user : familyMembers) {
                    UserWalkChallengeDto userChallenge = UserWalkChallengeDto.builder()
                            .userId(user.getUserId())
                            .challengeId(challenge.getChallengeId())
                            .distance(0.0f)  // 초기 거리는 0
                            .build();

                    try {
                        walkChallengeMapper.insertUserWalkChallenge(userChallenge);
                        log.info("사용자 {} 챌린지 참가 등록 성공", user.getUserId());
                    } catch (Exception e) {
                        // 이미 등록된 사용자는 무시 (중복 키 등의 예외 처리)
                        log.warn("사용자 {} 챌린지 참가 등록 실패: {}", user.getUserId(), e.getMessage());
                    }
                }

                // 참가자 정보 다시 조회 (방금 등록한 정보 포함)
                participantsData = walkChallengeMapper.getChallengeParticipantsWithRank(challenge.getChallengeId());

                // 다시 처리
                participants = participantsData.stream()
                        .map(data -> {
                            Integer userId = (Integer) data.get("user_id");
                            String nickname = (String) data.get("nickname");
                            String profileColor = (String) data.get("profileColor");
                            Float distanceCompleted = (Float) data.get("distanceCompleted");
                            Integer rank = ((Number) data.get("user_rank")).intValue();

                            UserDto userDto = userService.getUserDtoById(userId);
                            return ParticipantDto.builder()
                                    .userId(userId)
                                    .nickname(userDto.getUserNickname())
                                    .familySequence(userDto.getUserFamilySequence())
                                    .zodiacName(userDto.getUserZodiacName())
                                    .distanceCompleted(distanceCompleted != null ? distanceCompleted : 0.0f)
                                    .rank(rank)
                                    .build();
                        })
                        .collect(Collectors.toList());
            } else {
                // 가족 구성원이 없는 경우 빈 목록
                participants = List.of();
            }
        }

        // 응답 DTO 생성
        return WalkChallengeResponseDto.builder()
                .challengeId(challenge.getChallengeId())
                .startDate(challenge.getStartDate())
                .targetDistance(challenge.getTargetDistance())
                .progressPercentage(progressPercentage)
                .participants(participants)
                .build();
    }
}