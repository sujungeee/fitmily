package com.d208.fitmily.domain.health.mapper;


import com.d208.fitmily.domain.health.dto.AddHealthRequestDto;
import com.d208.fitmily.domain.health.dto.HealthInsertDto;
import com.d208.fitmily.domain.health.dto.HealthResponseDto;
import com.d208.fitmily.domain.health.dto.UpdateHealthResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HealthMapper  {

    // 건강상태 추가
//    int insertHealth(AddHealthRequestDto dto);
    void insertHealth(HealthInsertDto dto);


    //건강상태 조회
    HealthResponseDto selectLatestByUserId(Integer userId);

    //건강상태 수정(int 쓰는 이유: int는 update된 row 수를 반환, 예외처리 가능)
    int updateHealth(UpdateHealthResponseDto dto);



}
