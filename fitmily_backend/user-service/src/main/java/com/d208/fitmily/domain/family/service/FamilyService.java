package com.d208.fitmily.domain.family.service;

import com.d208.fitmily.domain.exercise.entity.Exercise;
import com.d208.fitmily.domain.exercise.entity.ExerciseGoal;
import com.d208.fitmily.domain.exercise.mapper.ExerciseGoalMapper;
import com.d208.fitmily.domain.exercise.mapper.ExerciseMapper;
import com.d208.fitmily.domain.family.dto.FamilyCalendarResponse;
import com.d208.fitmily.domain.family.dto.FamilyDailyExerciseResponse;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.d208.fitmily.domain.chat.dto.MessageRequestDTO;
import com.d208.fitmily.domain.chat.service.ChatMessageService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyService {

    private final FamilyMapper familyMapper;
    private final ExerciseMapper exerciseMapper;
    private final ExerciseGoalMapper exerciseGoalMapper;
    private final HealthMapper healthMapper;
    private final ObjectMapper objectMapper;
    private final ChatMessageService chatMessageService;

    private static final int MAX_FAMILY_MEMBERS = 6;

    @Transactional
    public int createFamily(String familyName, int userId) {  // userId 매개변수 추가
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

        // 패밀리 생성자를 해당 패밀리의 첫 멤버로 등록 (sequence = 1)
        familyMapper.updateUserFamilyIdAndSequence(userId, family.getFamilyId(), 1);

        // 채팅방 초기화 - 시스템 메시지 전송
        initializeChat(String.valueOf(family.getFamilyId()), "system");

        return family.getFamilyId();
    }

    /**
     * 패밀리 가입
     */
    @Transactional
    public int joinFamily(String inviteCode, int userId) {
        try {
            // 입력 파라미터 디버깅 로그
            System.out.println("초대 코드: " + inviteCode);
            System.out.println("사용자 ID: " + userId);

            // 패밀리 조회
            Family family = familyMapper.findByInviteCode(inviteCode);

            // 조회 결과 디버깅 로그
            System.out.println("조회된 패밀리: " + (family != null ? family.getFamilyId() : "null"));

            // 패밀리 존재 여부 확인
            if (family == null) {
                throw new CustomException(ErrorCode.INVALID_INVITE_CODE);
            }

            int familyId = family.getFamilyId();

            // 현재 패밀리의 최대 순서 번호 조회
            Integer maxSequence = familyMapper.findMaxFamilySequence(familyId);
            int newSequence = (maxSequence != null && maxSequence > 0) ? maxSequence + 1 : 1;

            System.out.println("새로 할당할 패밀리 순서: " + newSequence);

            // 사용자의 패밀리 ID와 순서 함께 업데이트
            familyMapper.updateUserFamilyIdAndSequence(userId, familyId, newSequence);

            // 패밀리 인원 수 증가
            familyMapper.incrementFamilyPeople(familyId);

            return familyId;
        } catch (Exception e) {
            // 예외 발생 시 스택 트레이스 출력
            System.out.println("패밀리 가입 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;  // 예외 다시 던지기
        }
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
     * 패밀리 홈 대시보드 조회 - 목표 달성률 표시 형식으로 변경
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
        List<FamilyDashboardResponse.FamilyMember> memberList = new ArrayList<>();

        for (User member : familyMembers) {
            // 사용자의 해당 일자 목표 정보 조회
            List<ExerciseGoal> exerciseGoals = exerciseGoalMapper.findUserGoalsByDate(member.getUserId(), date);

            // 안전 처리: exerciseGoals가 null이면 빈 목록으로 설정
            if (exerciseGoals == null) {
                exerciseGoals = new ArrayList<>();
            }

            // 목표 정보를 GoalInfo 객체로 변환
            List<FamilyDashboardResponse.GoalInfo> goalInfoList = new ArrayList<>();
            int completedGoals = 0;

            for (ExerciseGoal goal : exerciseGoals) {
                FamilyDashboardResponse.GoalInfo goalInfo = FamilyDashboardResponse.GoalInfo.builder()
                        .exerciseGoalId(goal.getExerciseGoalId())
                        .exerciseGoalName(goal.getExerciseGoalName())
                        .exerciseGoalValue((int) goal.getExerciseGoalValue())
                        .exerciseGoalProgress(goal.getExerciseGoalProgress())
                        .build();

                goalInfoList.add(goalInfo);

                // 완료된 목표 카운트
                if (goal.getExerciseGoalProgress() >= 100) {
                    completedGoals++;
                }
            }

            // 총 진행률 계산 (모든 목표의 평균 진행률)
            int totalGoals = exerciseGoals.size();
            int progressRate = totalGoals > 0 ?
                    (int)Math.round((double)completedGoals / totalGoals * 100) : 0;

            // 구성원 정보 생성
            FamilyDashboardResponse.FamilyMember memberInfo = FamilyDashboardResponse.FamilyMember.builder()
                    .userId(member.getUserId())
                    .userNickname(member.getUserNickname())
                    .userZodiacName(member.getUserZodiacName())
                    .userFamilySequence(member.getUserFamilySequence())
                    .goals(goalInfoList)
                    .totalProgressRate(progressRate)
                    .build();

            memberList.add(memberInfo);
        }

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
            // 사용자의 최신 건강 정보 조회
            HealthResponseDto healthInfo = healthMapper.selectLatestByUserId(member.getUserId());

            // 기본값 초기화 (float 타입으로 변경)
            float height = 0.0f;
            float weight = 0.0f;
            float bmi = 0.0f;
            List<String> fiveMajorDiseases = Collections.emptyList();
            List<String> otherDiseases = Collections.emptyList();

            // 건강 정보가 있으면 실제 값 사용
            if (healthInfo != null) {
                // 실제 값이 있는 경우에만 기본값 대신 사용 (float로 변환)
                height = healthInfo.getHeight() != null ? healthInfo.getHeight().floatValue() : height;
                weight = healthInfo.getWeight() != null ? healthInfo.getWeight().floatValue() : weight;
                bmi = healthInfo.getBmi() != null ? healthInfo.getBmi().floatValue() : bmi;

                // JSON 문자열을 리스트로 변환
                fiveMajorDiseases = parseJsonList(healthInfo.getFiveMajorDiseases());
                otherDiseases = parseJsonList(healthInfo.getOtherDiseases());
            }

            // 모든 구성원에 대해 응답 객체 생성
            FamilyHealthStatusResponse.MemberHealthInfo memberHealthInfo = FamilyHealthStatusResponse.MemberHealthInfo.builder()
                    .userId(member.getUserId())
                    .userNickname(member.getUserNickname())
                    .userBirth(member.getUserBirth())
                    .userGender(member.getUserGender())
                    .userZodiacName(member.getUserZodiacName())
                    .userFamilySequence(member.getUserFamilySequence())
                    .healthHeight(height)
                    .healthWeight(weight)
                    .healthBmi(bmi)
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

    /**
     * 패밀리 월간 달성 여부 조회
     */
    @Transactional(readOnly = true)
    public FamilyCalendarResponse getFamilyCalendar(int familyId, int year, String month) {
        // 패밀리 존재 확인
        Family family = familyMapper.findById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        }

        // 패밀리 구성원 목록 조회
        List<User> familyMembers = familyMapper.findFamilyMembers(familyId);

        // 멤버 정보 생성
        List<FamilyCalendarResponse.MemberInfo> memberInfoList = familyMembers.stream()
                .map(member -> FamilyCalendarResponse.MemberInfo.builder()
                        .userId(member.getUserId())
                        .userName(member.getUserNickname())
                        .userFamilysequence(member.getUserFamilySequence())
                        .build())
                .collect(Collectors.toList());

        // 월의 첫날과 마지막날 계산
        LocalDate startDate = LocalDate.of(year, Integer.parseInt(month), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 목표 달성한 날짜 및 사용자 조회
        List<FamilyCalendarResponse.CalendarEntry> calendarEntries = new ArrayList<>();

        // 각 사용자별, 날짜별로 목표 달성 여부 확인
        for (User member : familyMembers) {
            LocalDate currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {
                String dateString = currentDate.format(DateTimeFormatter.ISO_DATE);
                boolean allGoalsCompleted = false;

                try {
                    // 1. 특정 날짜의 운동 목표 조회
                    List<ExerciseGoal> goals = exerciseGoalMapper.findUserGoalsByDate(member.getUserId(), dateString);

                    // 2. 목표가 있고 모든 목표가 100% 달성되었는지 확인
                    if (goals != null && !goals.isEmpty()) {
                        allGoalsCompleted = true; // 기본값 true로 설정

                        for (ExerciseGoal goal : goals) {
                            // 하나라도 100%가 안 되면 allGoalsCompleted = false
                            if (goal.getExerciseGoalProgress() < 100) {
                                allGoalsCompleted = false;
                                break;
                            }
                        }
                    }

                    // 3. 운동 목표 테이블에 데이터가 없는 경우(운동을 안 했거나 목표 설정이 안 됨)
                    else {
                        allGoalsCompleted = false;
                    }

                } catch (Exception e) {
                    log.error("운동 데이터 조회 중 오류: {}", e.getMessage());
                    allGoalsCompleted = false;
                }

                // 모든 목표를 달성한 경우에만 캘린더에 표시
                if (allGoalsCompleted) {
                    calendarEntries.add(FamilyCalendarResponse.CalendarEntry.builder()
                            .userId(member.getUserId())
                            .date(dateString)
                            .userNickname(member.getUserNickname())
                            .userFamilySequence(member.getUserFamilySequence())
                            .build());
                }

                currentDate = currentDate.plusDays(1);
            }
        }

        return FamilyCalendarResponse.builder()
                .members(memberInfoList)
                .calendar(calendarEntries)
                .build();
    }


    // 사용자의 특정 날짜 운동 완료 여부 확인 (기존 Exercise 클래스 활용)
    private boolean checkDailyExerciseCompletion(int userId, String date) {
        // 사용자의 해당 일자 운동 기록 조회
        List<Exercise> exercises = exerciseMapper.findUserExercisesByDate(userId, date);

        if (exercises.isEmpty()) {
            return false;  // 운동 기록이 없으면 달성 실패
        }

        // 각 운동별로 목표 달성 여부 확인
        int completedExercises = 0;

        for (Exercise exercise : exercises) {
            // 운동 종류별 목표치 설정 (실제로는 DB에서 가져오거나 설정에 따라 결정)
            int targetCount = getExerciseTarget(exercise.getExerciseName());

            // 목표 달성 여부 확인
            if (exercise.getExerciseCount() >= targetCount) {
                completedExercises++;
            }
        }

        // 모든 운동이 목표를 달성했는지 확인
        return completedExercises == exercises.size() && !exercises.isEmpty();
    }

    // 운동 종류별 목표치 설정 (임시 하드코딩, 실제로는 DB에서 가져오는 것이 좋음)
    private int getExerciseTarget(String exerciseName) {
        switch (exerciseName.toLowerCase()) {
            case "스쿼트":
                return 20;
            case "팔굽혀펴기":
                return 15;
            case "윗몸일으키기":
                return 25;
            case "달리기":
            case "조깅":
                return 30;  // 시간(분) 기준일 수 있음
            default:
                return 10;  // 기본값
        }
    }

    /**
     * 패밀리 일일 운동 기록 조회
     */
    @Transactional(readOnly = true)
    public FamilyDailyExerciseResponse getFamilyDailyExercise(int familyId, String date) {
        // 패밀리 존재 확인
        Family family = familyMapper.findById(familyId);
        if (family == null) {
            throw new CustomException(ErrorCode.FAMILY_NOT_FOUND);
        }

        // 패밀리 구성원 목록 조회
        List<User> familyMembers = familyMapper.findFamilyMembers(familyId);
        List<FamilyDailyExerciseResponse.MemberDailyExercise> memberList = new ArrayList<>();

        for (User member : familyMembers) {
            // 1. 해당 일자의 운동 목표 조회
            List<ExerciseGoal> goals = exerciseGoalMapper.findUserGoalsByDate(member.getUserId(), date);
            Map<String, ExerciseGoal> goalMap = new HashMap<>();

            // 운동명 -> 목표 객체 매핑
            if (goals != null) {
                for (ExerciseGoal goal : goals) {
                    goalMap.put(goal.getExerciseGoalName().toLowerCase(), goal);
                }
            }

            // 2. 해당 일자의 운동 기록 조회
            List<Exercise> exercises = exerciseMapper.findUserExercisesByDate(member.getUserId(), date);
            if (exercises == null) exercises = new ArrayList<>();

            // 3. 통계 집계 및 운동 정보 변환
            int totalCalories = 0;
            int totalTime = 0;
            int completedGoals = 0;
            int totalMatches = 0;

            List<FamilyDailyExerciseResponse.ExerciseInfo> exerciseInfoList = new ArrayList<>();

            for (Exercise exercise : exercises) {
                if (exercise == null) continue;

                // 칼로리와 시간 누적
                totalCalories += exercise.getExerciseCalories();
                totalTime += (exercise.getExerciseTime() != null) ? exercise.getExerciseTime() : 0;

                // 운동명에 해당하는 목표 찾기
                ExerciseGoal matchedGoal = goalMap.get(exercise.getExerciseName().toLowerCase());
                int goalValue = 0;

                if (matchedGoal != null) {
                    // 목표가 있으면 목표값 사용
                    goalValue = (int)matchedGoal.getExerciseGoalValue();
                    totalMatches++;

                    // 목표 달성 여부 확인
                    if (exercise.getExerciseCount() >= goalValue) {
                        completedGoals++;
                    }
                }

                // 산책 경로 이미지 조회
                String routeImg = "";
                if ("산책".equals(exercise.getExerciseName())) {
                    routeImg = exerciseMapper.findRouteImageByExerciseId(exercise.getExerciseId());
                    if (routeImg == null) routeImg = "";
                }

                // 운동 정보 생성
                FamilyDailyExerciseResponse.ExerciseInfo info = FamilyDailyExerciseResponse.ExerciseInfo.builder()
                        .exerciseId(exercise.getExerciseId())
                        .exerciseName(exercise.getExerciseName())
                        .exerciseRouteImg(routeImg)
                        .exerciseCount(exercise.getExerciseCount())
                        .exerciseGoalValue(goalValue)  // 매칭된 목표값 사용
                        .exerciseCalories(exercise.getExerciseCalories())
                        .exerciseTime(exercise.getExerciseTime() != null ? exercise.getExerciseTime() : 0)
                        .build();

                exerciseInfoList.add(info);
            }

            // 목표 진행률 계산 (매칭된 목표 중 달성한 것의 비율)
            int progressRate = totalMatches > 0 ?
                    (int)Math.round(((double)completedGoals / totalMatches) * 100) : 0;

            // 구성원별 정보 생성
            FamilyDailyExerciseResponse.MemberDailyExercise memberDaily =
                    FamilyDailyExerciseResponse.MemberDailyExercise.builder()
                            .userId(member.getUserId())
                            .userNickname(member.getUserNickname())
                            .userFamilySequence(member.getUserFamilySequence())
                            .exerciseGoalProgress(progressRate)
                            .totalCalories(totalCalories)
                            .totalTime(totalTime)
                            .exercises(exerciseInfoList)
                            .build();

            memberList.add(memberDaily);
        }

        return FamilyDailyExerciseResponse.builder()
                .members(memberList)
                .build();
    }



    //
    /**
     * 채팅방 초기화 - 시스템 메시지 전송
     */
    private void initializeChat(String familyId, String senderId) {
        try {
            // 환영 메시지 생성
            MessageRequestDTO welcomeMessage = new MessageRequestDTO();
            welcomeMessage.setMessageType("system");
            welcomeMessage.setContent("패밀리 채팅방이 생성되었습니다. 가족들과 함께 대화를 나눠보세요!");

            // 메시지 전송
            chatMessageService.sendMessage(familyId, welcomeMessage, senderId);

            // 로그 기록
            log.info("패밀리 채팅방 초기화 완료 - 패밀리 ID: {}", familyId);
        } catch (Exception e) {
            // 실패해도 패밀리 생성은 계속 진행
            log.error("패밀리 채팅방 초기화 실패 (무시됨)", e);
        }
    }

}