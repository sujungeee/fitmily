package com.d208.fitmily.health.service;


import com.d208.fitmily.health.dto.AddHealthRequestDto;
import com.d208.fitmily.health.entity.Health;
import com.d208.fitmily.health.mapper.HealthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthMapper healthMapper;

    public void addHealth(Integer userId, AddHealthRequestDto dto){
        Health health = Health.builder()
                .userId(userId)
                .healthBmi(dto.getHealthBmi())
                .healthHeight(dto.getHealthHeight())
                .healthWeight(dto.getHealthWeight())
                .healthBodyFatPercentage(dto.getHealthBodyFatPercentage())
                .healthDisease(dto.getHealthDisease())
                .build();

        healthMapper.insertHealth(health);
    }

}
