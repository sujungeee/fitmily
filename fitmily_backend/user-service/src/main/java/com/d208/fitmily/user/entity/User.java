package com.d208.fitmily.user.entity;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
public class User {
    private Integer id;
    private String loginId;
    private String password;
    private String nickname;
    private String birth;
    private String gender;
    private String refreshToken;
    private String role;
}