package com.d208.user_service.user.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.d208.user_service.user.dto.JoinRequestDTO;
import com.d208.user_service.user.entity.User;

@Service
@RequiredArgsConstructor
public class UserService {

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
}
