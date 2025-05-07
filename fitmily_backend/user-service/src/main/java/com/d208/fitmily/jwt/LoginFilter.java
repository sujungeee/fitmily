package com.d208.fitmily.jwt;

import com.d208.fitmily.user.dto.CustomUserDetails;
import com.d208.fitmily.user.dto.LoginRequestDTO;
import com.d208.fitmily.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ApplicationContext applicationContext;
//    private final UserService userService;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 여기 DTO 이름 수정!!
            LoginRequestDTO loginRequestDTO = objectMapper.readValue(request.getInputStream(), LoginRequestDTO.class);

            String loginId = loginRequestDTO.getLogin_id();
            String password = loginRequestDTO.getPassword();

            System.out.println("유저네임 : " + loginId);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password, null);
            return authenticationManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();

        Integer userId = details.getId();
        String userNickname = details.getNickname();

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");  //  권한이 비어있으면 기본값

        String accessToken = jwtUtil.createAccessToken(userId, role);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        // refreshToken DB 업데이트
        UserService userService = applicationContext.getBean(UserService.class);
        userService.updateRefreshToken(userId, refreshToken);


        // 응답 작성 , userId, userNickname, userProfileImg
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Map<String, Object> tokenMap = new LinkedHashMap<>(); //LinkedHashMap 쓴 이유:선입선출(FIFO)의 순서를 지켜서 출력위해
        tokenMap.put("userId", userId);
        tokenMap.put("userNickname", userNickname);
        tokenMap.put("accessToken", accessToken);
        tokenMap.put("refreshToken", refreshToken);

        new ObjectMapper().writeValue(response.getWriter(), tokenMap);
        System.out.println("로그인 성공");
    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
        System.out.println("로그인 실패");
    }
}