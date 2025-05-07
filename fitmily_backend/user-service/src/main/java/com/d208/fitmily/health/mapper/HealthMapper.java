package com.d208.fitmily.health.mapper;


import com.d208.fitmily.health.entity.Health;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthMapper  {

    // 건강상태 추가
    int insertHealth(Health health);


    Health selectLatestByUserId(Integer userId);

}
