package com.d208.user_service.user.service;

import com.d208.user_service.user.dto.CustomUserDetails;
import com.d208.user_service.user.entity.User;
import com.d208.user_service.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String loginId)
            throws UsernameNotFoundException {
        // 1) UserMapper로 조회
        User user = userMapper.findByLoginId(loginId);

        // 2) 없으면 예외 던지기
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다. loginId=" + loginId);
        }

        // 3) CustomUserDetails로 감싸서 반환
        return new CustomUserDetails(user);
    }

}