package com.d208.fitmily.domain.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 채팅 세션 관리 및 온라인 상태 처리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSessionService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 사용자 온라인 상태 등록 (채팅방 입장)
     */
    public void registerOnlineUser(String familyId, String userId) {
        try {
            if (userId == null || familyId == null) {
                log.warn("온라인 사용자 등록 실패: userId={}, familyId={}", userId, familyId);
                return;
            }

            // 채팅방 온라인 사용자 목록에 추가
            redisTemplate.opsForSet().add("family:" + familyId + ":subscribers", userId);
            log.info("채팅방 온라인 상태 등록: userId={}, familyId={}", userId, familyId);

            // 현재 온라인 사용자 목록 로깅
            logOnlineUsers(familyId);
        } catch (Exception e) {
            log.error("온라인 상태 등록 중 오류: {}", e.getMessage());
        }
    }

    /**
     * 사용자 온라인 상태 해제 (채팅방 퇴장)
     */
    public void unregisterOnlineUser(String familyId, String userId) {
        try {
            if (userId == null) {
                log.warn("온라인 사용자 해제 실패: userId is null");
                return;
            }

            if (familyId != null) {
                // 특정 채팅방에서만 제거
                redisTemplate.opsForSet().remove("family:" + familyId + ":subscribers", userId);
                log.info("특정 채팅방 온라인 상태 해제: userId={}, familyId={}", userId, familyId);
            } else {
                // 모든 채팅방에서 제거
                Set<String> keys = redisTemplate.keys("family:*:subscribers");
                if (keys != null) {
                    for (String key : keys) {
                        redisTemplate.opsForSet().remove(key, userId);
                    }
                }
                log.info("모든 채팅방 온라인 상태 해제: userId={}", userId);
            }

            // 변경 후 온라인 사용자 확인
            if (familyId != null) {
                logOnlineUsers(familyId);
            }
        } catch (Exception e) {
            log.error("온라인 상태 해제 중 오류: {}", e.getMessage());
        }
    }

    /**
     * 채팅방의 온라인 사용자 목록 조회
     */
    public Set<Object> getOnlineUsers(String familyId) {
        try {
            return redisTemplate.opsForSet().members("family:" + familyId + ":subscribers");
        } catch (Exception e) {
            log.error("온라인 사용자 조회 중 오류: {}", e.getMessage());
            return null;
        }
    }

    /**
     * WebSocket 연결 세션 정보 저장
     */
    public void saveSessionInfo(String sessionId, String userId) {
        if (sessionId != null && userId != null) {
            redisTemplate.opsForValue().set("chat:session:" + sessionId, userId, 24, TimeUnit.HOURS);
            log.debug("WebSocket 세션 정보 저장: sessionId={}, userId={}", sessionId, userId);
        }
    }

    /**
     * WebSocket 연결 세션 정보 삭제
     */
    public void removeSessionInfo(String sessionId) {
        if (sessionId != null) {
            String userId = getUserIdFromSession(sessionId);
            if (userId != null) {
                // 모든 채팅방에서 사용자 제거
                unregisterOnlineUser(null, userId);
            }

            // 세션 정보 삭제
            redisTemplate.delete("chat:session:" + sessionId);
            log.debug("WebSocket 세션 정보 삭제: sessionId={}", sessionId);
        }
    }

    /**
     * 세션 ID로 사용자 ID 조회
     */
    public String getUserIdFromSession(String sessionId) {
        if (sessionId == null) return null;

        try {
            Object userId = redisTemplate.opsForValue().get("chat:session:" + sessionId);
            return userId != null ? userId.toString() : null;
        } catch (Exception e) {
            log.error("세션에서 사용자 ID 조회 중 오류: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 채팅방 온라인 사용자 목록 로깅
     */
    private void logOnlineUsers(String familyId) {
        try {
            Set<Object> onlineUsers = getOnlineUsers(familyId);
            log.info("채팅방 온라인 사용자 현황: familyId={}, users={}",
                    familyId, onlineUsers);
        } catch (Exception e) {
            log.warn("온라인 사용자 로깅 실패: {}", e.getMessage());
        }
    }

    /**
     * 정기적으로 온라인 상태 정리 (5분마다 실행)
     */
    @Scheduled(fixedRate = 300000)
    public void cleanupOnlineUsers() {
        try {
            // 모든 채팅방 키 조회
            Set<String> keys = redisTemplate.keys("family:*:subscribers");
            if (keys == null || keys.isEmpty()) return;

            // 현재 활성 세션이 있는 사용자 목록 조회
            Set<String> sessionKeys = redisTemplate.keys("chat:session:*");
            Set<String> activeSessions = new HashSet<>();

            if (sessionKeys != null) {
                for (String sessionKey : sessionKeys) {
                    Object userId = redisTemplate.opsForValue().get(sessionKey);
                    if (userId != null) {
                        activeSessions.add(userId.toString());
                    }
                }
            }

            // 모든 채팅방 온라인 사용자 정리
            for (String key : keys) {
                Set<Object> onlineUsers = redisTemplate.opsForSet().members(key);
                if (onlineUsers == null) continue;

                for (Object user : onlineUsers) {
                    String userId = user.toString();
                    if (!activeSessions.contains(userId)) {
                        redisTemplate.opsForSet().remove(key, userId);
                    }
                }
            }

            // 정리 후 상태 로깅
            for (String key : keys) {
                Set<Object> onlineUsers = redisTemplate.opsForSet().members(key);
                log.info("정리 후 온라인 사용자: key={}, users={}", key, onlineUsers);
            }
        } catch (Exception e) {
            log.error("온라인 상태 정리 중 오류: {}", e.getMessage(), e);
        }
    }
}