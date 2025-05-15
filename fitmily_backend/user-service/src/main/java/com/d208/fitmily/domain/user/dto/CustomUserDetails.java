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
        return List.of(new SimpleGrantedAuthority(userEntity.getRole()));
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

    public String getGender() {
        return userEntity.getGender();
    }

    public String getgetZodiacName() { return userEntity.getZodiacName();}

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