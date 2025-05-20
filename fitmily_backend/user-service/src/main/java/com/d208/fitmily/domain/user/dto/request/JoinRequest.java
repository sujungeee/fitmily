package com.d208.fitmily.domain.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequest {

    private String loginId;
    private String password;
    private String nickname;
    private String birth;
    private Integer gender;

}