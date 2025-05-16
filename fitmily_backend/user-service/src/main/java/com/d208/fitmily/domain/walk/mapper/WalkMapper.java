package com.d208.fitmily.domain.walk.mapper;

import com.d208.fitmily.domain.walk.dto.StopWalkDto;
import com.d208.fitmily.domain.walk.dto.WalkResponseDto;
import com.d208.fitmily.domain.walk.entity.Walk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WalkMapper {

    // 산책 중지시 데이터 저장
    int insertStopWalk(StopWalkDto walk);

    // 산책 기록 조회
    List<WalkResponseDto> selectWalks(Map<String, Object> params);

    //산책 목표 존재 조회
    Boolean walkGoalExists(Integer userId);

}
