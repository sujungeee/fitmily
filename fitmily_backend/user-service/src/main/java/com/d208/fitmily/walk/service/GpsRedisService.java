package com.d208.fitmily.walk.service;

import com.d208.fitmily.walk.dto.GpsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GpsRedisService {

    private final StringRedisTemplate redisTemplate; // 레디스랑 문자열 기반으로 데이터 주고 받는 템플릿
    private final ObjectMapper objectMapper;         // Jackson의 JSON 변환기

    public void saveGps(GpsDto gpsDto) {
        try {
            // key 설정
            String key = "walk:gps:" + gpsDto.getUserId();

            //  GpsDto → JSON 문자열로 변환
            String jsonValue = objectMapper.writeValueAsString(gpsDto);

            //  Redis List의 끝에 추가
            redisTemplate.opsForList().rightPush(key, jsonValue);

        } catch (JsonProcessingException e) {
            // 예외 발생 시 로그
            throw new RuntimeException("GPS 데이터를 JSON으로 변환 실패", e);
        }
    }

}
