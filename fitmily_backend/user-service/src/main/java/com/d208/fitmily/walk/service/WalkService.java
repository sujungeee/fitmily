package com.d208.fitmily.walk.service;

import com.d208.fitmily.user.entity.User;
import com.d208.fitmily.user.service.UserService;
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
    private final UserService userService;

    //칼로리 계산해서 추가해야함
    @Transactional
    public void endWalk(Integer userId, EndWalkRequestDto dto){
        User user = userService.getUserById(userId);


        Walk walk = Walk.builder()
                .userId(userId)
                .walkRouteImg(dto.getWalkRouteImg())
                .walkStartTime(dto.getWalkStartTime())
                .walkEndTime(dto.getWalkEndTime())
                .walkDistance(dto.getWalkDistance())
                .walkHeartRate(dto.getWalkHeartRate())
                .build();
        walkMapper.insertStopWalk(walk);
    }

}



