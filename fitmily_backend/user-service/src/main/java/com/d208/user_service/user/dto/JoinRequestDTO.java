package com.d208.user_service.user.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JoinRequestDTO {

    private String login_id;
    private String password;
    private String nickname;
    private String birth;
    private String gender;

}