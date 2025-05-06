package com.d208.user_service.config;

import com.d208.user_service.jwt.JWTFilter;
import com.d208.user_service.jwt.JWTUtil;
import com.d208.user_service.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;
    private final ApplicationContext applicationContext;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

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
                        .requestMatchers( "/swagger-ui/**").permitAll()
                        .requestMatchers( "/v3/api-docs/**").permitAll()
                        .requestMatchers( "/api-docs/**").permitAll()
                        .requestMatchers( "/swagger-ui.html").permitAll()

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
