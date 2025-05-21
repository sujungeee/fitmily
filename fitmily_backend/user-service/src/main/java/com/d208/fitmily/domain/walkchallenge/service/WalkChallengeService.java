package com.d208.fitmily.domain.walkchallenge.service;

import com.d208.fitmily.domain.fcm.service.FcmService;
import com.d208.fitmily.domain.notification.entity.Notification;
import com.d208.fitmily.domain.notification.mapper.NotificationMapper;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final FcmService fcmService;
    private final NotificationMapper notificationMapper;

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
            try {
                createChallengeForFamily(familyId, today);
            } catch (Exception e) {
                log.error("가족 ID {}의 챌린지 생성 중 오류 발생: {}", familyId, e.getMessage());
            }
        }
    }

    /**
     * 특정 가족에 대한 주간 챌린지 생성
     */
    @Transactional
    public void createChallengeForFamily(Integer familyId, LocalDate startDate) {
        try {
            // 가족 구성원 수 조회
            int memberCount = familyMapper.findFamilyMembers(familyId).size();
            if (memberCount == 0) {
                log.warn("가족 ID {}에 구성원이 없습니다. 챌린지를 생성하지 않습니다.", familyId);
                return;
            }

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

                // 달성률 계산 (0으로 나누기 방지)
                float achievementRate = previousTarget > 0 ? (totalDistance / previousTarget) * 100 : 0;

                // 목표 조정
                if (achievementRate < 50) {
                    // 달성률 50% 미만: 목표 감소 (0.9배)
                    targetDistance = Math.round(baseTargetDistance * 0.9f);
                } else if (achievementRate >= 120) {
                    // 달성률 120% 이상: 목표 증가 (1.1배)
                    targetDistance = Math.round(baseTargetDistance * 1.1f);
                }
            }

            // 항상 정수 값으로 저장, 최소값은 1
            targetDistance = Math.max(1, targetDistance);

            // 챌린지 생성
            WalkChallengeDto newChallenge = WalkChallengeDto.builder()
                    .familyId(familyId)
                    .targetDistance(targetDistance)
                    .startDate(startDate)
                    .build();

            walkChallengeMapper.insertWalkChallenge(newChallenge);
            log.info("가족 ID {}의 새 챌린지 생성 완료 (ID: {}, 목표거리: {}km)",
                    familyId, newChallenge.getChallengeId(), targetDistance);

            // 가족 구성원들을 챌린지에 등록
            registerFamilyMembersToChallenge(familyId, newChallenge.getChallengeId());
        } catch (Exception e) {
            log.error("가족 ID {}의 챌린지 생성 중 오류 발생: {}", familyId, e.getMessage(), e);
            throw e;
        }
    }

    /**
     * 산책 완료 시 챌린지 거리 업데이트
     */
    @Transactional
    public void updateChallengeDistance(StopWalkDto walkDto) {
        try {
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
            log.debug("사용자 {} 챌린지 거리 {}km 업데이트 완료", userId, distance);
        } catch (Exception e) {
            log.error("챌린지 거리 업데이트 중 오류: {}", e.getMessage());
            // 예외를 삼키고 실패를 로그만 남김 (산책 기록 저장은 계속 진행)
        }
    }

    /**
     * 현재 진행 중인 챌린지 조회
     */
    @Transactional
    public void syncChallengeDistances(Integer challengeId) {
        try {
            // 챌린지에 참여한 모든 사용자의 거리를 walk 테이블 기준으로 업데이트
            walkChallengeMapper.syncUserWalkChallengeDistances(challengeId);
            log.debug("챌린지 ID {} 거리 동기화 완료", challengeId);
        } catch (Exception e) {
            log.error("챌린지 거리 동기화 중 오류: {}", e.getMessage());
        }
    }

    /**
     * 현재 챌린지 정보 조회
     */
    public WalkChallengeResponseDto getCurrentChallenge(Integer familyId) {
        try {
            // 현재 활성화된 챌린지 조회
            WalkChallengeDto challenge = walkChallengeMapper.findActiveChallenge(familyId);

            if (challenge == null) {
                log.info("가족 ID {}의 활성화된 챌린지 없음", familyId);
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

            // 참가자 데이터가 없으면 가족 구성원 자동 등록
            if (participantsData == null || participantsData.isEmpty()) {
                registerFamilyMembersToChallenge(familyId, challenge.getChallengeId());
                participantsData = walkChallengeMapper.getChallengeParticipantsWithRank(challenge.getChallengeId());
            }

            // 참가자 데이터 변환
            List<ParticipantDto> participants = convertParticipantData(participantsData);

            // 응답 DTO 생성
            return WalkChallengeResponseDto.builder()
                    .challengeId(challenge.getChallengeId())
                    .startDate(challenge.getStartDate())
                    .targetDistance(challenge.getTargetDistance())
                    .progressPercentage(progressPercentage)
                    .participants(participants)
                    .build();
        } catch (Exception e) {
            log.error("챌린지 정보 조회 중 오류: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 매주 일요일 오후 9시에 챌린지 종료 체크 및 알림 발송
     */
    @Scheduled(cron = "0 0 21 * * SUN")
    @Transactional
    public void checkChallengeCompletionAndNotify() {
        log.info("주간 챌린지 종료 체크 시작");

        List<Integer> allFamilyIds = walkChallengeMapper.getAllFamilyIds();
        LocalDate today = LocalDate.now();

        for (Integer familyId : allFamilyIds) {
            try {
                WalkChallengeDto challenge = walkChallengeMapper.findActiveChallenge(familyId);

                if (challenge != null) {
                    LocalDate startDate = challenge.getStartDate();
                    LocalDate endDate = startDate.plusDays(7);

                    if (today.isEqual(endDate) || today.isAfter(endDate)) {
                        walkChallengeMapper.syncUserWalkChallengeDistances(challenge.getChallengeId());

                        Float totalDistance = walkChallengeMapper.getTotalDistanceByChallenge(challenge.getChallengeId());
                        if (totalDistance == null) totalDistance = 0.0f;

                        sendChallengeCompletionNotification(familyId, challenge, totalDistance);
                    }
                }
            } catch (Exception e) {
                log.error("챌린지 종료 처리 중 오류 발생: familyId={}, error={}", familyId, e.getMessage(), e);
            }
        }

        log.info("주간 챌린지 종료 체크 완료");
    }

    /**
     * 사용자가 가족에 가입할 때 호출되는 메서드
     * 가족에 챌린지가 없으면 새로 생성하고, 있으면 목표 거리를 업데이트함
     */
    @Transactional
    public void handleUserJoinFamily(Integer userId, Integer familyId) {
        if (familyId == null) {
            log.warn("가족 ID가 null입니다. 챌린지를 처리하지 않습니다.");
            return;
        }

        try {
            // 현재 활성화된 챌린지 조회
            WalkChallengeDto activeChallenge = walkChallengeMapper.findActiveChallenge(familyId);

            if (activeChallenge == null) {
                // 활성화된 챌린지가 없으면 새로 생성
                log.info("가족 ID {}의 활성화된 챌린지가 없습니다. 새 챌린지를 생성합니다.", familyId);
                createChallengeForFamily(familyId, LocalDate.now());
                // 새로 생성된 챌린지 조회
                activeChallenge = walkChallengeMapper.findActiveChallenge(familyId);

                if (activeChallenge == null) {
                    log.error("챌린지 생성 후에도 활성화된 챌린지를 찾을 수 없습니다.");
                    return;
                }
            } else {
                // 활성화된 챌린지가 있으면 목표 거리 업데이트 (5km 추가)
                int newTargetDistance = activeChallenge.getTargetDistance() + DEFAULT_INDIVIDUAL_TARGET_KM;
                log.info("가족 ID {}의 챌린지 목표 거리 업데이트: {} -> {}km",
                        familyId, activeChallenge.getTargetDistance(), newTargetDistance);

                // 챌린지 업데이트 (목표 거리만 변경)
                walkChallengeMapper.updateChallengeTargetDistance(activeChallenge.getChallengeId(), newTargetDistance);
            }

            // 사용자를 챌린지에 등록
            addUserToChallenge(userId, activeChallenge.getChallengeId());
        } catch (Exception e) {
            log.error("사용자 {}의 가족 {} 가입 시 챌린지 처리 중 오류: {}", userId, familyId, e.getMessage(), e);
            // 가족 가입 자체는 계속 진행되도록 예외를 throw하지 않음
        }
    }

    /**
     * 사용자를 챌린지에 등록
     */
    private void addUserToChallenge(Integer userId, Integer challengeId) {
        try {
            // 사용자가 이미 챌린지에 등록되어 있는지 확인
            int count = walkChallengeMapper.countUserInChallenge(userId, challengeId);
            if (count == 0) {
                // 새 사용자를 챌린지에 등록
                UserWalkChallengeDto userChallenge = UserWalkChallengeDto.builder()
                        .userId(userId)
                        .challengeId(challengeId)
                        .distance(0.0f)  // 초기 거리는 0
                        .build();

                walkChallengeMapper.insertUserWalkChallenge(userChallenge);
                log.info("사용자 {} 챌린지 {} 참가 등록 성공", userId, challengeId);
            } else {
                log.info("사용자 {}는 이미 챌린지 {}에 등록되어 있습니다", userId, challengeId);
            }
        } catch (Exception e) {
            log.error("사용자 {} 챌린지 {} 참가 등록 실패: {}", userId, challengeId, e.getMessage());
            // 예외를 잡아서 로그만 남기고 상위로 전파하지 않음
        }
    }

    /**
     * 가족 구성원을 챌린지에 등록
     */
    private void registerFamilyMembersToChallenge(Integer familyId, Integer challengeId) {
        List<User> familyMembers = familyMapper.findFamilyMembers(familyId);
        if (familyMembers == null || familyMembers.isEmpty()) {
            log.warn("가족 ID {}에 구성원이 없습니다.", familyId);
            return;
        }

        log.info("가족 구성원 자동 등록 시작: 가족 ID {}, 챌린지 ID {}", familyId, challengeId);

        for (User user : familyMembers) {
            addUserToChallenge(user.getUserId(), challengeId);
        }
    }

    /**
     * 참가자 데이터 변환
     */
    private List<ParticipantDto> convertParticipantData(List<Map<String, Object>> participantsData) {
        if (participantsData == null || participantsData.isEmpty()) {
            return List.of();
        }

        return participantsData.stream()
                .map(data -> {
                    try {
                        Integer userId = (Integer) data.get("user_id");
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
                    } catch (Exception e) {
                        log.error("참가자 데이터 변환 중 오류: {}", e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null) // null 값 필터링
                .collect(Collectors.toList());
    }

    /**
     * 챌린지 완료 알림 발송
     */
    private void sendChallengeCompletionNotification(Integer familyId, WalkChallengeDto challenge, Float totalDistance) {
        try {
            LocalDate startDate = challenge.getStartDate();
            LocalDate endDate = startDate.plusDays(7);

            // 달성율 계산
            int achievementRate = (int)((totalDistance / challenge.getTargetDistance()) * 100);

            // 알림 메시지 생성
            String notificationContent;
            if (achievementRate >= 100) {
                notificationContent = String.format("목표 %dkm 중 %.1fkm 달성! 목표를 완료했어요. 결과를 확인해보세요!",
                        challenge.getTargetDistance(), totalDistance);
            } else {
                notificationContent = String.format("목표 %dkm 중 %.1fkm 달성! 달성률: %d%%. 다음 챌린지도 함께해요!",
                        challenge.getTargetDistance(), totalDistance, achievementRate);
            }

            // 가족 구성원의 notification 테이블에 알림 저장
            List<User> familyMembers = familyMapper.findFamilyMembers(familyId);
            for (User user : familyMembers) {
                // 현재 시간
                LocalDateTime now = LocalDateTime.now();

                // notification 테이블에 알림 추가
                com.d208.fitmily.domain.notification.entity.Notification dbNotification =
                        new com.d208.fitmily.domain.notification.entity.Notification();
                dbNotification.setUserId(user.getUserId());
                dbNotification.setNotificationType("CHALLENGE");  // CHALLENGE_COMPLETED 대신 CHALLENGE로 통일
                dbNotification.setNotificationSenderId(null);  // 시스템 발송은 0으로 설정
                dbNotification.setNotificationReceiverId(user.getUserId());
                dbNotification.setNotificationResourceId(challenge.getChallengeId()); // challengeId 저장
                dbNotification.setNotificationContent(notificationContent);
                dbNotification.setNotificationIsRead(0);
                dbNotification.setNotificationCreatedAt(now);
                dbNotification.setNotificationUpdatedAt(now);

                notificationMapper.insertNotification(dbNotification);
                log.info("챌린지 종료 알림 저장 완료: userId={}, challengeId={}", user.getUserId(), challenge.getChallengeId());
            }

            // FCM 알림 전송
            fcmService.sendChallengeCompletionNotification(
                    familyId,
                    challenge.getChallengeId(),
                    startDate.format(DateTimeFormatter.ISO_DATE),
                    endDate.format(DateTimeFormatter.ISO_DATE),
                    challenge.getTargetDistance(),
                    totalDistance
            );

            log.info("챌린지 종료 알림 발송 완료: familyId={}, challengeId={}, targetDistance={}, totalDistance={}",
                    familyId, challenge.getChallengeId(), challenge.getTargetDistance(), totalDistance);
        } catch (Exception e) {
            log.error("챌린지 종료 알림 발송 중 오류: {}", e.getMessage());
        }
    }
    /**
     * 테스트용 챌린지 종료 알림 발송
     */
    @Transactional
    public void testChallengeCompletion(Integer familyId, WalkChallengeDto challenge, Float totalDistance) {
        // 챌린지 종료 알림만 강제로 발송
        sendChallengeCompletionNotification(familyId, challenge, totalDistance);
        log.info("테스트용 챌린지 종료 알림 발송: familyId={}, challengeId={}", familyId, challenge.getChallengeId());
    }
}