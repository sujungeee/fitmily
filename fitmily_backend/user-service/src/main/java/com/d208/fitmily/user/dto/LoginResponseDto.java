package com.d208.fitmily.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private Integer userId;
    private String username;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
