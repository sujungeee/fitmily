package com.d208.fitmily.walk.mapper;

import com.d208.fitmily.walk.entity.Walk;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WalkMapper {

    int insertStopWalk(Walk walk);


}
