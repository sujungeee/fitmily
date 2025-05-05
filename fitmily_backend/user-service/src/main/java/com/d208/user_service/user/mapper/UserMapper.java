package com.d208.user_service.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper

public interface UserMapper {

    boolean existsByLoginId(@Param("loginId") String loginId);


}
