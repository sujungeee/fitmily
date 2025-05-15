package com.d208.fitmily.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequestDTO {

    private String loginId;
    private String password;
    private String nickname;
    private String birth;
    private Integer gender;

}