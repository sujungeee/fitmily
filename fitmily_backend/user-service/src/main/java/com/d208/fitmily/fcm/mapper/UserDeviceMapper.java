package com.d208.fitmily.fcm.mapper;

import com.d208.fitmily.fcm.dto.FCMTokenDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface UserDeviceMapper {
    // 더미 구현
    default List<FCMTokenDTO> selectActiveFCMTokensByUserIds(@Param("userIds") List<String> userIds) {
        // 더미 데이터 반환
        List<FCMTokenDTO> tokens = new ArrayList<>();
        for (String userId : userIds) {
            tokens.add(new FCMTokenDTO(userId, "dummy_token_" + userId));
        }
        return tokens;
    }

    default void insertFCMToken(@Param("userId") String userId,
                                @Param("deviceId") String deviceId,
                                @Param("fcmToken") String fcmToken) {
        // 더미 구현
        System.out.println("FCM 토큰 등록 (더미): " + userId + ", " + deviceId + ", " + fcmToken);
    }

    default void updateFCMToken(@Param("userId") String userId,
                                @Param("deviceId") String deviceId,
                                @Param("fcmToken") String fcmToken) {
        // 더미 구현
        System.out.println("FCM 토큰 업데이트 (더미): " + userId + ", " + deviceId + ", " + fcmToken);
    }
}