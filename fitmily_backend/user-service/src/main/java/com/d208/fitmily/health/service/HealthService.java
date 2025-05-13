package com.d208.fitmily.health.service;

import com.d208.fitmily.health.dto.AddHealthRequestDto;
import com.d208.fitmily.health.dto.HealthResponseDto;
import com.d208.fitmily.health.dto.UpdateHealthRequestDto;
import com.d208.fitmily.health.dto.UpdateHealthResponseDto;
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


        dto.setUserId(userId);
        dto.setBmi((float) (Math.floor(bmi * 10) / 10));

//        Health health = Health.builder()
//                .userId(userId)
//                .bmi((float) (Math.floor(bmi * 10) / 10))
//                .height(dto.getHeight())
//                .weight(dto.getWeight())
//                .otherDiseases(dto.getOtherDiseases())
//                .fiveMajorDiseases(dto.getFiveMajorDiseases())
//                .build();

        int result = healthMapper.insertHealth(dto);
    }

    //건강 상태 조회
    public HealthResponseDto getLatestHealth(Integer userId){
        return healthMapper.selectLatestByUserId(userId);
    }

    public void updateHealth(Integer userId , UpdateHealthRequestDto dto){
        HealthResponseDto existing = healthMapper.selectLatestByUserId(userId);

        Float height = dto.getHeight() != null ? dto.getHeight() : existing.getHeight();
        Float weight = dto.getWeight() != null ? dto.getWeight() : existing.getWeight();
        Float bmi = weight / ((height / 100f) * (height / 100f));
        bmi = (float) (Math.floor(bmi * 10) / 10);


        UpdateHealthResponseDto updateDto = new UpdateHealthResponseDto(
                userId, bmi, height, weight,
                dto.getOtherDiseases() != null ? dto.getOtherDiseases() : existing.getOtherDiseases(),
                dto.getFiveMajorDiseases() != null ? dto.getFiveMajorDiseases() : existing.getFiveMajorDiseases()
        );

        healthMapper.updateHealth(updateDto);
    }
}
