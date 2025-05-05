package com.d208.user_service.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.d208.user_service.user.dto.JoinRequestDTO;
import com.d208.user_service.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {


    // v 아이디 중복 체크
    public boolean isUsernameDuplicate(String username) {
        return userMapper.existsByLoginId(username);
    }

    // 회원가입
    public void joinprocess(JoinRequestDTO dto){

        if (userMapper.existsByLoginId(dto.getLogin_id())) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATED);
        }
        //  User 객체 생성
        User user = new User();
        user.setLoginId(dto.getLogin_id());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setBirth(dto.getBirth());
        user.setGender(dto.getGender());
        user.setRole("ROLE_USER");

        //  INSERT
        userMapper.insert(user);
    }

    // v 로그인시 refresh 토큰 저장
    @Transactional
    public void updateRefreshToken(Integer userId, String refreshToken) {
        int rows = userMapper.updateRefreshToken(userId, refreshToken);
        if (rows == 0) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
    }

    // v 로그아웃 (리프레시 토큰 삭제)
    @Transactional
    public void clearRefreshToken(Integer userId) {
        userMapper.clearRefreshToken(userId);
    }


    /* reissue 하기 */
    public String reissueAccessToken(String refreshToken) {

        //  토큰 유효성 검사
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        // 토큰 매칭 검사
        Integer userId = jwtUtil.getUserId(refreshToken);
        if (!userMapper.existsByIdAndRefreshToken(userId, refreshToken)) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISMATCH);
        }
        // 엑세스토큰 재생성
        return jwtUtil.createAccessToken(userId, "ROLE_USER");
    }
}
