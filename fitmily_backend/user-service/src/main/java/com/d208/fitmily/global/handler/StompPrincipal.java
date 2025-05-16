package com.d208.fitmily.global.handler;

import lombok.RequiredArgsConstructor;

import java.security.Principal;


@RequiredArgsConstructor
public class StompPrincipal implements Principal {  //Principal은 "현재 인증된 사용자 누구냐?

    private final String name; // userId를 문자열로 저장

    @Override
    public String getName() { // Principal는 이 메서드 하나뿐임
        return name;     // Principal.getName() → userId
    }
}
