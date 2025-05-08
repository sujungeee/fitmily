package com.d208.fitmily.walk.mapper;

import com.d208.fitmily.walk.entity.Walk;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WalkMapper {

    int insertStopWalk(Walk walk);

    List<Walk> selectWalks(Map<String, Object> params);


}
