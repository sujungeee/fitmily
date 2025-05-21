//package com.d208.fitmily.global.config;
//
//import jakarta.annotation.PostConstruct;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class SecretLogger {
//
//    @Value("${spring.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${cloud.aws.region}")
//    private String region;
//
//    @PostConstruct
//    public void logJwtSecret() {
//        System.out.println("✅✅✅ JWT 시크릿 = " + jwtSecret);
//        System.out.println("✅✅✅ region = " + region);
//
//    }
//}
//
