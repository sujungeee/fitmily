package com.d208.fitmily.domain.health.service;

import com.d208.fitmily.domain.health.dto.*;
import com.d208.fitmily.domain.health.mapper.HealthMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthMapper healthMapper;
    private final ObjectMapper objectMapper;

    // 건강상태 추가
    public void addHealth(Integer userId, AddHealthRequestDto dto) throws JsonProcessingException {


        //bmi 계산해서 넣음
        float heightM = dto.getHeight() / 100f;
        float bmi = dto.getWeight() / (heightM * heightM);


        dto.setUserId(userId);
        dto.setBmi((float) (Math.floor(bmi * 10) / 10));

        String otherJson  = objectMapper.writeValueAsString(dto.getOtherDiseases());
        String majorJson  = objectMapper.writeValueAsString(dto.getFiveMajorDiseases());

        HealthInsertDto insertDto = new HealthInsertDto();
        insertDto.setUserId(userId);
        insertDto.setHeight(dto.getHeight());
        insertDto.setWeight(dto.getWeight());
        insertDto.setBmi(bmi);
        insertDto.setHealthOtherDiseasesJson(otherJson);
        insertDto.setHealthFiveMajorDiseasesJson(majorJson);

        healthMapper.insertHealth(insertDto);
    }

    //건강 상태 조회
    public HealthResponseDto getLatestHealth(Integer userId) {
        HealthResponseDto raw = healthMapper.selectLatestByUserId(userId);

        //등록된 db가 없을때
        if (raw == null) {
            return HealthResponseDto.builder()
                    .otherDiseases("[]")
                    .fiveMajorDiseases("[]")
                    .otherDiseasesList(Collections.emptyList())
                    .fiveMajorDiseasesList(Collections.emptyList())
                    .build();
        }

        try {
            if (raw.getOtherDiseases() != null) {
                List<String> otherList = objectMapper.readValue(
                        raw.getOtherDiseases(),
                        new TypeReference<List<String>>() {});
                raw.setOtherDiseasesList(otherList);
            }

            if (raw.getFiveMajorDiseases() != null) {
                List<String> majorList = objectMapper.readValue(
                        raw.getFiveMajorDiseases(),
                        new TypeReference<List<String>>() {});
                raw.setFiveMajorDiseasesList(majorList);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 필요 시 로깅 또는 사용자 예외 전환
        }

        return raw;
    }


    //건강 상태 수정
    public void updateHealth(Integer userId , UpdateHealthRequestDto dto) throws JsonProcessingException {
        HealthResponseDto existing = healthMapper.selectLatestByUserId(userId);

        Float height = dto.getHeight() != null ? dto.getHeight() : existing.getHeight();
        Float weight = dto.getWeight() != null ? dto.getWeight() : existing.getWeight();
        Float bmi = weight / ((height / 100f) * (height / 100f));
        bmi = (float) (Math.floor(bmi * 10) / 10);

        // JSON 직렬화
        String otherDiseasesJson = objectMapper.writeValueAsString(dto.getOtherDiseases());
        String majorDiseasesJson = objectMapper.writeValueAsString(dto.getFiveMajorDiseases());


        UpdateHealthResponseDto updateDto = UpdateHealthResponseDto.builder()
                .userId(userId)
                .bmi(bmi)
                .height(height)
                .weight(weight)
                .otherDiseases(otherDiseasesJson)
                .fiveMajorDiseases(majorDiseasesJson)
                .build();

        healthMapper.updateHealth(updateDto);
    }
}
