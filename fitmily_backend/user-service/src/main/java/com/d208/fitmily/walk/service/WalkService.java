package com.d208.fitmily.walk.service;

import com.d208.fitmily.walk.dto.EndWalkRequestDto;
import com.d208.fitmily.walk.entity.Walk;
import com.d208.fitmily.walk.mapper.WalkMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WalkService {

    private final WalkMapper walkMapper;


    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        Walk walk = Walk.builder()
                .userId(userId)
                .walkRouteImg(dto.getWalkRouteImg())
                .walkStartTime(dto.getWalkStartTime())
                .walkEndTime(dto.getWalkEndTime())
                .walkDistance(dto.getWalkDistance())
                .walkHeartRate(dto.getWalkHeartRate())
//                .stepCount(dto.getStepCount())
                .build();
        walkMapper.insertStopWalk(walk);
    }

}



