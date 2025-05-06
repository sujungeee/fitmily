package com.d208.user_service.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.security.x509.X509CertImpl;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JWTUtil {

    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        //객체타입으로 저장
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public Integer getUserId(String token) {
        Number idNum = Jwts.parser()                    // jjwt 0.12.x 이하
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("userId", Number.class);               // Number로 먼저 꺼냄
        return idNum.intValue();                        // int로 변환해서 반환
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public String getRole(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    //만료되었는지 확인하는 함수
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    //토큰 생성 메소드
    public String createJwt(Integer userId, String role, String tokenType, Long expiredMs) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("role", role) // refresh에서는 생략해도 됨
                .claim("token_type", tokenType) // 토큰 타입
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
    public String createAccessToken(Integer userId, String role) {
        return createJwt(userId, role, "access", 1000L * 60 * 15); // 15분
    }

    public String createRefreshToken(Integer userId) {
        return createJwt(userId, null, "refresh", 1000L * 60 * 60 * 24 * 14); // 2주
    }

}
