package com.d208.fitmily.walk.service;

import com.d208.fitmily.walk.dto.GpsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GpsRedisService {

    private final StringRedisTemplate redisTemplate; // 레디스랑 문자열 기반으로 데이터 주고 받는 템플릿
    private final ObjectMapper objectMapper;         // Jackson의 JSON 변환기

    //gps 데이터 저장
    public void saveGps( Integer userId,GpsDto gpsDto) {
        try {
            // key 설정
            String key = "walk:gps:" + userId;

            //  GpsDto → JSON 문자열로 변환
            String jsonValue = objectMapper.writeValueAsString(gpsDto);

            //  Redis List의 끝에 추가
            redisTemplate.opsForList().rightPush(key, jsonValue);

        } catch (JsonProcessingException e) {
            // 예외 발생 시 로그
            throw new RuntimeException("GPS 데이터를 JSON으로 변환 실패", e);
        }
    }

    //gps 데이터 조회
    public List<GpsDto> getGpsListByUserId(Integer userId) {
        String key = "walk:gps:" + userId;

        // Redis에서 가져온 GPS 데이터의 JSON 목록조회
        List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);

        //역직렬화 해줘야함(json -> gpsDto)
        List<GpsDto> result = new ArrayList<>();
        for (String json : jsonList) {
            try {
                GpsDto gps = objectMapper.readValue(json, GpsDto.class); // json -> gpsDto
                result.add(gps);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("GPS 데이터 역직렬화 실패", e);
            }
        }
        return result;
    }
}
