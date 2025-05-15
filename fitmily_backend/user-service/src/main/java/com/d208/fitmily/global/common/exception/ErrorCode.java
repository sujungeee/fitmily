package com.d208.fitmily.global.common.exception;

import org.springframework.http.HttpStatus;


public enum ErrorCode {
    INTERNAL_SERVER_ERROR(5000, "서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USERNAME_DUPLICATED(4001, "이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(4011, "토큰이 없습니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH(4012, "토큰이 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(4002, "유저 아이디가 없습니다.", HttpStatus.UNAUTHORIZED),

    // 채팅 관련 오류 코드
    CHAT_ACCESS_DENIED(4101, "채팅방 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    CHAT_MESSAGE_SEND_FAILED(4102, "메시지 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CHAT_MESSAGE_READ_FAILED(4103, "메시지 읽음 처리에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),


    // FCM 관련 오류 코드
    FCM_TOKEN_REGISTRATION_FAILED(4201, "FCM 토큰 등록에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FCM_NOTIFICATION_FAILED(4202, "FCM 알림 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FCM_TOKEN_INVALID(4203, "유효하지 않은 FCM 토큰입니다.", HttpStatus.BAD_REQUEST),

    // 패밀리 관련
    INVALID_INVITE_CODE(4301, "유효하지 않은 초대 코드입니다", HttpStatus.BAD_REQUEST),
    FAMILY_MEMBER_LIMIT_EXCEEDED(4302, "패밀리 최대 인원(6명)을 초과했습니다", HttpStatus.BAD_REQUEST);

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
