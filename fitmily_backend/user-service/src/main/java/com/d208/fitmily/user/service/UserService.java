package com.d208.fitmily.user.service;


import com.d208.fitmily.common.exception.BusinessException;
import com.d208.fitmily.common.exception.ErrorCode;
import com.d208.fitmily.jwt.JWTUtil;
import com.d208.fitmily.user.dto.ReissueResponseDto;
import com.d208.fitmily.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.d208.fitmily.user.dto.JoinRequestDTO;
import com.d208.fitmily.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;


    /* 아이디 중복 체크 */
    public boolean isUsernameDuplicate(String username) {
        return userMapper.existsByLoginId(username);
    }

    /* 회원가입 */
    public void joinprocess(JoinRequestDTO dto){

        if (userMapper.existsByLoginId(dto.getLogin_id())) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATED);
        }

        User user = new User();
        user.setLoginId(dto.getLogin_id());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setBirth(dto.getBirth());
        user.setGender(dto.getGender());
        user.setRole("ROLE_USER");


        userMapper.insert(user);
    }

    /*  로그인 (refresh 토큰 저장) */
    @Transactional
    public void updateRefreshToken(Integer userId, String refreshToken) {
        int rows = userMapper.updateRefreshToken(userId, refreshToken);
        if (rows == 0) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }

//    public LoginResponseDto buildLoginResponse(CustomUserDetails user,String accessToken, String refreshToken) {
//        return new LoginResponseDto(
//                user.getId(),
//                user.getUsername(),
//                user.getNickname(),
//                accessToken,
//                refreshToken
//        );
//    }

    /* 로그아웃 (리프레시 토큰 삭제) */
    @Transactional
    public void clearRefreshToken(Integer userId) {
        userMapper.clearRefreshToken(userId);
    }


    /* reissue 하기 */
    public ReissueResponseDto  reissueAccessToken(String refreshToken) {

        //  refreshToken 토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // refreshToken 토큰 매칭 검사
        Integer userId = jwtUtil.getUserId(refreshToken);
        if (!userMapper.existsByIdAndRefreshToken(userId, refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }

        String newAccessToken = jwtUtil.createAccessToken(userId, "ROLE_USER");
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // 리프레시 토큰 업데이트
        userMapper.updateRefreshToken(userId, newRefreshToken);

        // 액세스+리프레시 토큰을 묶어서 반환
        return new ReissueResponseDto(newAccessToken, newRefreshToken);
    }
}
