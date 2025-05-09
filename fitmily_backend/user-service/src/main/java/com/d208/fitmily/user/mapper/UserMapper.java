package com.d208.fitmily.user.mapper;


import com.d208.fitmily.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper

public interface UserMapper {

    // 아이디 중복 조회
    boolean existsByLoginId(@Param("loginId") String loginId);

    // 로그인 아이디로 User 조회
    User findByLoginId(@Param("loginId") String loginId);

    // 회원 정보 삽입 (INSERT)
    int insert(User user);

    // 리프레시 토큰 업데이트
    int updateRefreshToken(
            @Param("userId") Integer userId,
            @Param("refreshToken") String refreshToken
    );

    //리프레시 토큰 지우기 (로그아웃)
    int clearRefreshToken(@Param("userId") Integer userId);

    // 아이디+토큰 일치 여부 확인
    boolean existsByIdAndRefreshToken(
            @Param("userId") Integer userId,
            @Param("refreshToken") String refreshToken
    );

    // userId 로 조회
    User selectById(Integer userId);


}
