package com.d208.fitmily.health.service;

import com.d208.fitmily.health.dto.AddHealthRequestDto;
import com.d208.fitmily.health.dto.HealthResponseDto;
import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.health.mapper.HealthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthMapper healthMapper;

    // 건강상태 추가
    public void addHealth(Integer userId, AddHealthRequestDto dto){

        //bmi 계산해서 넣음
        float heightM = dto.getHeight() / 100f;
        float bmi = dto.getWeight() / (heightM * heightM);

        Health health = Health.builder()
                .userId(userId)
                .bmi((float) (Math.floor(bmi * 10) / 10))
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .otherDiseases(dto.getOtherDiseases())
                .fiveMajorDiseases(dto.getFiveMajorDiseases())
                .build();

        healthMapper.insertHealth(health);
    }

    //건강 상태 조회
    public HealthResponseDto getLatestHealth(Integer userId){
        Health health = healthMapper.selectLatestByUserId(userId);

        return new HealthResponseDto(
                health.getHealthId(),
                health.getBmi(),
                health.getHeight(),
                health.getWeight(),
                health.getOtherDiseases(),
                health.getFiveMajorDiseases(),
                health.getCreatedAt(),
                health.getUpdatedAt()
        );
    }
}
