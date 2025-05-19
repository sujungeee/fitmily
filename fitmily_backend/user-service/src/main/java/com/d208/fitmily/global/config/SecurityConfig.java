package com.d208.fitmily.global.config;

import com.d208.fitmily.global.jwt.JWTFilter;
import com.d208.fitmily.global.jwt.JWTUtil;
import com.d208.fitmily.global.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final ApplicationContext applicationContext;

    public static int getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("인증 정보가 없습니다.");
        }

        Object principal = authentication.getPrincipal();
        try {
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                if (username != null && !username.isEmpty()) {
                    return Integer.parseInt(username);
                }
            } else if (principal != null) {
                String principalStr = principal.toString();
                if (principalStr != null && !principalStr.isEmpty()) {
                    return Integer.parseInt(principalStr);
                }
            }
            throw new RuntimeException("사용자 ID를 찾을 수 없습니다.");
        } catch (NumberFormatException e) {
            throw new RuntimeException("사용자 ID 형식이 올바르지 않습니다: " + principal);
        }
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*"); // 모든 origin 허용 (운영환경에선 도메인 제한 필요) http://k12d208.p.ssafy.io
        configuration.addAllowedMethod("*");        // GET, POST, PUT, DELETE 등 모두 허용
        configuration.addAllowedHeader("*");        // 모든 헤더 허용
        configuration.setAllowCredentials(true);    // 쿠키, 인증정보 포함 여부 (필요에 따라 true/false)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(withDefaults());
        //JWT 방식(상태 less)이라 csrf 공격에 방어하지 않아도됨
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/users").permitAll() //이 경로에 대해서는 모두 허용
                        .requestMatchers("/api/auth/reissue").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/users/check-id").permitAll()

                        // swagger 설정
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers( "/v3/api-docs/**").permitAll()
                        .requestMatchers( "/api-docs/**").permitAll()
                        .requestMatchers( "/swagger-ui.html").permitAll()
//
                        //chat-test.html 허용 코드 3가지
                        .requestMatchers("/api/ws-connect/**").permitAll()
                        .requestMatchers("/chat-test.html").permitAll()
                        .requestMatchers("/.well-known/**").permitAll()

                        .requestMatchers("/api/ws-connect/**").permitAll()

                        .requestMatchers( "/*").permitAll()


//                       .permitAll() 대신 -> .hasRole("ADMIN") 붙이면 ADMIN이라는 접근을 가진사람만 접근가능해짐
                        .anyRequest().authenticated()); //나머지는 로그인한 사용자만 접근할 수 있게

        http.addFilterBefore(
                new JWTFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class
        );


        // 로그인 처리 필터 등록
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, applicationContext);
        loginFilter.setFilterProcessesUrl("/api/auth/login"); //login 필터의 경로를 변경 해줌  /api/login 으로
        http
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
