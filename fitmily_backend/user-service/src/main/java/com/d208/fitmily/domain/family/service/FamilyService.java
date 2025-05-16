package com.d208.fitmily.domain.family.service;

import com.d208.fitmily.domain.exercise.entity.Exercise;
import com.d208.fitmily.domain.exercise.mapper.ExerciseMapper;
import com.d208.fitmily.domain.family.dto.FamilyDashboardResponse;
import com.d208.fitmily.domain.family.dto.FamilyHealthStatusResponse;
import com.d208.fitmily.domain.family.entity.Family;
import com.d208.fitmily.domain.family.mapper.FamilyMapper;
import com.d208.fitmily.domain.health.dto.HealthResponseDto;
import com.d208.fitmily.domain.health.mapper.HealthMapper;
import com.d208.fitmily.domain.user.entity.User;
import com.d208.fitmily.global.common.exception.CustomException;
import com.d208.fitmily.global.common.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyMapper familyMapper;
    private final ExerciseMapper exerciseMapper;
    private final HealthMapper healthMapper;
    private final ObjectMapper objectMapper;

    private static final int MAX_FAMILY_MEMBERS = 6;

    @Transactional
    public int createFamily(String familyName) {
        Family family = new Family();
        family.setFamilyName(familyName);

        // 초대 코드 생성
        String inviteCode = UUID.randomUUID().toString().substring(0, 8);
        family.setFamilyInviteCode(inviteCode);

        // 초기 설정
        family.setFamilyPeople(1); // 처음 생성 시 1명
        LocalDateTime now = LocalDateTime.now();
        family.setFamilyCreatedAt(now);
        family.setFamilyUpdatedAt(now);

        // DB에 저장
        familyMapper.createFamily(family);

        return family.getFamilyId();
    }

    /**
     * 패밀리 가입
     */
    @Transactional
    public int joinFamily(String inviteCode, int userId) {
        // 초대 코드로 패밀리 조회
        Family family = familyMapper.findByInviteCode(inviteCode);
        if (family == null) {
            throw new com.d208.fitmily.global.common.exception.CustomException(ErrorCode.INVALID_INVITE_CODE);
        }

        // 최대 인원 체크 (6명)
        if (family.getFamilyPeople() >= MAX_FAMILY_MEMBERS) {
            throw new com.d208.fitmily.global.common.exception.CustomException(ErrorCode.FAMILY_MEMBER_LIMIT_EXCEEDED);
        }

        // 사용자 패밀리 업데이트
        familyMapper.updateUserFamilyId(userId, family.getFamilyId());

        // 패밀리 인원 수 증가
        familyMapper.incrementFamilyPeople(family.getFamilyId());

        return family.getFamilyId();
    }

    /**
     * 패밀리 조회
     */
    public Family getFamily(int familyId) {
        Family family = familyMapper.findById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        }
        return family;
    }

    /**
     * 패밀리 홈 대시보드 조회 - Exercise 테이블 사용
     */
    @Transactional(readOnly = true)
    public FamilyDashboardResponse getFamilyDashboard(int familyId, String date) {
        // 패밀리 존재 확인
        Family family = familyMapper.findById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        }

        // 패밀리 구성원 목록 조회
        List<User> familyMembers = familyMapper.findFamilyMembers(familyId);

        // 각 구성원의 운동 정보 조회
        List<FamilyDashboardResponse.FamilyMember> memberList = new ArrayList<>();

        for (User member : familyMembers) {
            // 사용자의 해당 일자 운동 목록 조회
            List<Exercise> exercises = exerciseMapper.findUserExercisesByDate(member.getUserId(), date);

            // 운동 정보 변환
            List<FamilyDashboardResponse.ExerciseInfo> exerciseInfoList = exercises.stream()
                    .map(exercise -> FamilyDashboardResponse.ExerciseInfo.builder()
                            .exerciseId(exercise.getExerciseId())
                            .exerciseName(exercise.getExerciseName())
                            .exerciseTime(exercise.getExerciseTime())
                            .exerciseCount(exercise.getExerciseCount())
                            .exerciseCalories(exercise.getExerciseCalories())
                            .build())
                    .collect(Collectors.toList());

            // 총 운동 칼로리 조회 (진행률 대체)
            int totalCalories = exerciseMapper.calculateUserTotalCalories(member.getUserId(), date);

            // 진행률 계산 (예: 목표 칼로리를 500으로 가정하고 비율 계산)
            int targetCalories = 500; // 고정값 또는 DB에서 가져올 수 있음
            int progressRate = Math.min(totalCalories * 100 / Math.max(1, targetCalories), 100);

            // 구성원 정보 생성
            FamilyDashboardResponse.FamilyMember memberInfo = FamilyDashboardResponse.FamilyMember.builder()
                    .userId(member.getUserId())
                    .userNickname(member.getUserNickname())
                    .userZodiacName(member.getUserZodiacName())
                    .userFamilySequence(member.getUserFamilySequence())
                    .exercises(exerciseInfoList)  // goals → exercises
                    .totalProgressRate(progressRate)
                    .build();

            memberList.add(memberInfo);
        }

        // 응답 데이터 생성
        return FamilyDashboardResponse.builder()
                .date(date)
                .members(memberList)
                .build();
    }

    /**
     * 패밀리 건강 상태 조회
     */
    @Transactional(readOnly = true)
    public FamilyHealthStatusResponse getFamilyHealthStatus(int familyId) {
        // 패밀리 존재 확인
        Family family = familyMapper.findById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        }

        // 패밀리 구성원 목록 조회
        List<User> familyMembers = familyMapper.findFamilyMembers(familyId);

        // 각 구성원의 건강 정보 조회
        List<FamilyHealthStatusResponse.MemberHealthInfo> memberHealthInfoList = new ArrayList<>();

        for (User member : familyMembers) {
            // 사용자의 최신 건강 정보 조회 (기존 HealthMapper 사용)
            HealthResponseDto healthInfo = healthMapper.selectLatestByUserId(member.getUserId());

            // 건강 정보가 없는 사용자는 건너뜀
            if (healthInfo == null) {
                continue;
            }

            // JSON 문자열을 리스트로 변환
            List<String> fiveMajorDiseases = parseJsonList(healthInfo.getHealthFiveMajorDiseases());
            List<String> otherDiseases = parseJsonList(healthInfo.getHealthOtherDiseases());

            // 사용자 건강 정보 생성
            FamilyHealthStatusResponse.MemberHealthInfo memberHealthInfo = FamilyHealthStatusResponse.MemberHealthInfo.builder()
                    .userId(member.getUserId())
                    .userNickname(member.getUserNickname())
                    .userBirth(member.getUserBirth())
                    .userGender(member.getUserGender())
                    .healthHeight(healthInfo.getHealthHeight())
                    .healthWeight(healthInfo.getHealthWeight())
                    .healthBmi(healthInfo.getHealthBmi())
                    .healthFiveMajorDiseases(fiveMajorDiseases)
                    .healthOtherDiseases(otherDiseases)
                    .build();

            memberHealthInfoList.add(memberHealthInfo);
        }

        // 응답 데이터 생성
        return FamilyHealthStatusResponse.builder()
                .members(memberHealthInfoList)
                .build();
    }

    /**
     * JSON 문자열을 List<String>으로 변환
     */
    private List<String> parseJsonList(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

}