package com.d208.user_service.common.exception;

import org.springframework.http.HttpStatus;


public enum ErrorCode {
    INTERNAL_SERVER_ERROR(5000, "서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_DUPLICATED(4001, "이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4011, "토큰이 없습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH(4012, "토큰이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(4002, "유저 아이디가 없습니다.", HttpStatus.UNAUTHORIZED)

    ;
    private final int        code;
    private final String     message;
    private final HttpStatus status;

    ErrorCode(int code, String message, HttpStatus status) {
        this.code    = code;
        this.message = message;
        this.status  = status;
    }
    public int getCode()         { return code; }
    public String getMessage()   { return message; }
    public HttpStatus getStatus(){ return status; }
}
