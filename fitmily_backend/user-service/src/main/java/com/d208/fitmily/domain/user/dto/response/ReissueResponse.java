package com.d208.fitmily.domain.user.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReissueResponse {
    private String accessToken;
    private String refreshToken;

}

