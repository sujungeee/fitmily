package com.d208.user_service.common.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;  // 성공 여부
    private int     code;     // 응답 코드
    private String  message;  // 설명 메시지
    private T       data;     // 실제 데이터(Payload)

    // 성공 응답
    public static <T> ApiResponse<T> ok(T data, String message) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), message, data);
    }
    // 에러 응답
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
}