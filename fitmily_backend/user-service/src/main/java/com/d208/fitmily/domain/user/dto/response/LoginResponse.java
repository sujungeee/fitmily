package com.d208.fitmily.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Integer userId;
    private String username;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
