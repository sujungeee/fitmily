package user_service.user_service.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public ApiResponse<Void> join(@RequestBody JoinRequestDTO joinRequestDTO) {
        userService.joinprocess(joinRequestDTO);
        return ApiResponse.ok(null, "회원가입 성공");
    }
}
