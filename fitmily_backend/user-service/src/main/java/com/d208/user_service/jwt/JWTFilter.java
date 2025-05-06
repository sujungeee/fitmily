package com.d208.user_service.jwt;


import com.d208.user_service.user.dto.CustomUserDetails;
import com.d208.user_service.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JWTFilter.class);
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        log.debug("JWTFilter 시작 → 요청 URI=[{}], Authorization 헤더=[{}]", uri, authHeader);

        // swagger-ui, API-docs 경로는 검증 스킵
        if (uri.startsWith("/swagger-ui") || uri.startsWith("/v3/api-docs")) {
            log.debug("  → Swagger 관련 경로, 필터 건너뜀");
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더 유무 확인
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("  → Bearer 토큰이 없음. SecurityContext 미설정 상태로 다음 필터로 이동");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 파싱·만료 검사
        String token = authHeader.substring(7);
        boolean expired;
        try {
            expired = jwtUtil.isExpired(token);
        } catch (ExpiredJwtException e) {
            expired = true;
        }
        log.debug("  → 토큰 추출 완료, 만료 여부=[{}]", expired);

        if (expired) {
            log.debug("  → 토큰이 만료됨. 인증 정보 설정 없이 다음 필터로 이동");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 유효 → 사용자 정보 가져와서 Authentication 설정
        Integer  userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);
        log.debug("  → 토큰 유효, userId=[{}]", userId);

        // (권장) 실제 DB에서 UserEntity 조회 후 사용하세요
        User user = new User();
        user.setId(userId);
        user.setPassword("temppassword");
        user.setRole(role);


        CustomUserDetails details = new CustomUserDetails(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                details, null, details.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        log.debug("  → SecurityContext에 인증 정보 저장: [{}]", auth);

        filterChain.doFilter(request, response);
        log.debug("JWTFilter 종료 → SecurityContext=[{}]",
                SecurityContextHolder.getContext().getAuthentication());
    }
}

// 매번 요청이 들어올때마다 필터를 통해서 검증하고 인증 정보를 SecurityContextHolder에 저장한다 (왜냐? stateless 이기 때문에)
// stateless (jwt 방식) 을 사용하는 이유는 ? 메모리 아낄 수 있음, 서버가 많아져도 공유 필요 x, 클라이언트 자유도 높아짐, csrf 위험 x,
