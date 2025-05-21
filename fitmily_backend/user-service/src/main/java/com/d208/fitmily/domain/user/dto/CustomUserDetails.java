package com.d208.fitmily.domain.user.dto;


import com.d208.fitmily.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User userEntity;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // null이나 빈 문자열 체크 추가
        String role = userEntity.getRole();
        if (role == null || role.trim().isEmpty()) {
            role = "ROLE_USER"; // 기본 역할 설정
        }
        return List.of(new SimpleGrantedAuthority(role));
    }


    public Integer getId() {
        return userEntity.getUserId();
    }

    @Override
    public String getPassword() {

        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getLoginId();
    }

    public String getNickname() {
        return userEntity.getNickname();
    }

    public String getBirth() {
        return userEntity.getBirth();
    }

    public Integer getGender() {
        return userEntity.getGender();
    }

    public String getZodiacName(){
        return userEntity.getZodiacName();
    }

    public Integer getfamilyId() { return userEntity.getFamilyId();}



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}