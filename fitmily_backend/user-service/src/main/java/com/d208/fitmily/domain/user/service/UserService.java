package com.d208.fitmily.domain.user.service;



import com.d208.fitmily.global.jwt.JWTUtil;
import com.d208.fitmily.domain.user.dto.ReissueResponseDto;
import com.d208.fitmily.domain.user.mapper.UserMapper;
import com.d208.fitmily.domain.walk.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.d208.fitmily.domain.user.dto.JoinRequestDTO;
import com.d208.fitmily.domain.user.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTUtil jwtUtil;

    public static final String[] ZODIACS = {
            "Monkey", "Rooster", "Dog", "Pig", "Rat", "Ox", "Tiger", "Rabbit", "Dragon", "Snake", "Horse", "Sheep"
    };


    /* 회원가입 */ //
    public void joinprocess(JoinRequestDTO dto){

//        if (userMapper.existsByLoginId(dto.getLoginId())) {
//            throw new BusinessException(ErrorCode.USERNAME_DUPLICATED);
//        }

        //생년월일별 띠 계산
        String birth = dto.getBirth();
        int year = Integer.parseInt(birth.substring(0, 4));
        String zodiac = ZODIACS[year % 12];


        User user = new User();
        user.setLoginId(dto.getLoginId());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setBirth(dto.getBirth());
        user.setGender(dto.getGender());
        user.setRole("ROLE_USER");
        user.setFamilySequence(0);
        user.setRefreshToken("");
        user.setZodiacName(zodiac);

        userMapper.insert(user);
    }

    /*  로그인 (refresh 토큰 저장) */
    @Transactional
    public void updateRefreshToken(Integer userId, String refreshToken) {
        int rows = userMapper.updateRefreshToken(userId, refreshToken);
//        if (rows == 0) {
//            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
//        }
    }


    /* 로그아웃 (리프레시 토큰 삭제) */
    @Transactional
    public void clearRefreshToken(Integer userId) {
        userMapper.clearRefreshToken(userId);
    }


    /* reissue 하기 */
    public ReissueResponseDto  reissueAccessToken(String refreshToken) {

        //  refreshToken 토큰 유효성 검사
//        if (!jwtUtil.validateToken(refreshToken)) {
//            throw new BusinessException(ErrorCode.INVALID_TOKEN);
//        }

        // refreshToken 토큰 매칭 검사
        Integer userId = jwtUtil.getUserId(refreshToken);
//        if (!userMapper.existsByIdAndRefreshToken(userId, refreshToken)) {
//            throw new BusinessException(ErrorCode.REFRESH_TOKEN_MISMATCH);
//        }

        String newAccessToken = jwtUtil.createAccessToken(userId, "ROLE_USER");
        String newRefreshToken = jwtUtil.createRefreshToken(userId);

        // 리프레시 토큰 업데이트
        userMapper.updateRefreshToken(userId, newRefreshToken);

        // 액세스+리프레시 토큰을 묶어서 반환
        return new ReissueResponseDto(newAccessToken, newRefreshToken);
    }

    /* 유저 정보 조회 */
    public User getUserById(Integer userId){
        User user = userMapper.selectById(userId);
        return user;
    }

    public UserDto getUserDtoById(Integer userId) {
        return userMapper.getUserDtoById(userId);
    }

    /* 아이디 중복 체크 */
    public boolean isUsernameDuplicate(String username) {
        return userMapper.existsByLoginId(username);
    }

    /* familyId로 userId 전부 조회하기 */
    public List<Integer> getUserIdsByFamilyId(Integer familyId) {
        return userMapper.getUserIdsByFamilyId(familyId);
    }

    /* userId들에 따른 정보 한번에 조회 */
    public List<User> getUsersByIds(List<Integer> userIds) {
        return userMapper.getUsersByIds(userIds);
    }


}
