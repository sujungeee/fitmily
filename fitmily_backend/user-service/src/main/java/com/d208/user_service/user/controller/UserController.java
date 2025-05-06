package com.d208.user_service.user.controller;

import com.d208.user_service.common.response.ApiResponse;
import com.d208.user_service.user.dto.CustomUserDetails;
import com.d208.user_service.user.dto.JoinRequestDTO;
import com.d208.user_service.user.dto.LoginRequestDTO;
import com.d208.user_service.user.dto.ReissueRequestDto;
import com.d208.user_service.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Controller
@ResponseBody
@RequestMapping("/api")

@Tag(name = "회원 API", description = "로그인/회원가입 및 개인 정보")
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/users")
    public ApiResponse<Void> join(@RequestBody JoinRequestDTO joinRequestDTO) {
        userService.joinprocess(joinRequestDTO);
        return ApiResponse.ok(null, "회원가입 성공");
    }
    // 실제 인증은 LoginFilter에서 진행함 이건 그냥 swagger용 컨트롤러임
    @Operation(summary = "로그인", description = "- ID, PW로 로그인하고 AccessToken, RefreshToken 발급")
    @PostMapping("/auth/login")
    public void login(@RequestBody LoginRequestDTO request) {
    }

    @Operation(
            summary = "아이디 중복 확인", description = "- 동일 ID 존재하면 = true  \n- 동일 ID 존재하지 않으면 = false")
    @GetMapping("/users/check-id")
    public ApiResponse<Boolean> check(@RequestParam String username) {
        boolean isDuplicate = userService.isUsernameDuplicate(username);
        return ApiResponse.ok(isDuplicate,"중복 확인 성공");
    }

    @Operation(summary = "로그아웃", description = "- RefreshToken 삭제")
    @PostMapping("/auth/logout")
    public ApiResponse<Void> logout(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
        } else {
            Integer userId = principal.getId();
            userService.clearRefreshToken(userId);
        }
        return ApiResponse.ok(null,"로그아웃 성공");
    }

    @Operation(summary = "리이슈", description = "- access토큰 재발급")
    @PostMapping("auth/reissue")
    public ApiResponse<String> reissue(@RequestBody ReissueRequestDto reissueRequestDto) {
        String refreshToken = reissueRequestDto.getRefresh_token();
        String newAccessToken = userService.reissueAccessToken(refreshToken);
        return ApiResponse.ok(newAccessToken,"토큰 재발급 성공");
    }

}
