package com.d208.fitmily.domain.user.controller;


import com.d208.fitmily.domain.user.dto.*;
import com.d208.fitmily.domain.user.dto.request.JoinRequest;
import com.d208.fitmily.domain.user.dto.request.LoginRequest;
import com.d208.fitmily.domain.user.dto.request.ReissueRequest;
import com.d208.fitmily.domain.user.dto.response.ReissueResponse;
import com.d208.fitmily.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
    public ResponseEntity<Void> join(@RequestBody JoinRequest joinRequestDTO) {
        userService.joinprocess(joinRequestDTO);
        return ResponseEntity.ok(null);
    }

    // 실제 인증은 LoginFilter에서 진행함 이건 그냥 swagger용 컨트롤러임
    @Operation(summary = "로그인", description = "- ID, PW로 로그인하고 AccessToken, RefreshToken 발급")
    @PostMapping("/auth/login")
    public void login(@RequestBody LoginRequest request) {
    }

    @Operation(
            summary = "아이디 중복 확인", description = "- 동일 ID 존재하면 = true  \n- 동일 ID 존재하지 않으면 = false")
    @GetMapping("/users/check-id")
    public ResponseEntity<Boolean> check(@RequestParam String username) {
        boolean isDuplicate = userService.isUsernameDuplicate(username);
        return ResponseEntity.ok(isDuplicate);
    }

    @Operation(summary = "로그아웃", description = "- RefreshToken 삭제")
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null) {
        } else {
            Integer userId = principal.getId();
            userService.clearRefreshToken(userId);
        }
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "리이슈", description = "- access토큰 재발급")
    @PostMapping("/auth/reissue")
    public ResponseEntity<ReissueResponse> reissue(@RequestBody ReissueRequest reissueRequestDto) {
        String refreshToken = reissueRequestDto.getRefreshToken();
        ReissueResponse newToken = userService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(newToken);
    }
}
