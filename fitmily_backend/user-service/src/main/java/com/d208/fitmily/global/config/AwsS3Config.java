package com.d208.fitmily.global.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@Component
@ConfigurationProperties(prefix = "cloud.aws")
@Getter
@Setter
@Slf4j
public class AwsS3Config {

    @PostConstruct
    public void init() {
        System.out.println("✅ AWS region = " + region);
        System.out.println("✅ AWS accessKey = " + credentials.getAccessKey());
    }
//    cloud:
//    aws:
//    s3:
//    bucket: pop4u
//    stack.auto: false
//    region.static: us-east-1
//    credentials:
//    accessKey: AKIAXZ2CK3QV4WQ7TVW5
//    secretKey: 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
    private Credentials credentials;
    private String region;
    private String regionStatic; // region.static 속성 추가
    private S3 s3;
    private Stack stack;

    @Getter @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter @Setter
    public static class S3 {
        private String bucket;
    }

    @Getter @Setter
    public static class Stack {
        private boolean auto;
    }

    @Bean
    public S3Client s3Client() {
        try {
            // 실제 사용할 리전 결정 (regionStatic이 우선)
            String effectiveRegion = regionStatic != null ? regionStatic : region;

            log.info("AWS S3 클라이언트 초기화 시도 - 리전: {}, 버킷: {}", effectiveRegion,
                    s3 != null ? s3.getBucket() : "null");

            // 자격 증명 및 리전 유효성 검사
            if (credentials == null || credentials.getAccessKey() == null ||
                    credentials.getSecretKey() == null || effectiveRegion == null) {
                log.warn("AWS 자격 증명이 누락되었습니다. S3 기능이 비활성화됩니다.");
                return null;
            }

            return S3Client.builder()
                    .region(Region.of(effectiveRegion))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                            credentials.getAccessKey(),
                                            credentials.getSecretKey()
                                    )
                            )
                    )
                    .build();
        } catch (Exception e) {
            log.error("AWS S3 클라이언트 초기화 실패: {}", e.getMessage());
            return null;
        }
    }

    @Bean
    public S3Presigner s3Presigner() {
        try {
            // 실제 사용할 리전 결정 (regionStatic이 우선)
            String effectiveRegion = regionStatic != null ? regionStatic : region;

            // 자격 증명 및 리전 유효성 검사
            if (credentials == null || credentials.getAccessKey() == null ||
                    credentials.getSecretKey() == null || effectiveRegion == null) {
                log.warn("AWS 자격 증명이 누락되었습니다. S3 Presigner가 비활성화됩니다.");
                return null;
            }

            return S3Presigner.builder()
                    .region(Region.of(effectiveRegion))
                    .credentialsProvider(
                            StaticCredentialsProvider.create(
                                    AwsBasicCredentials.create(
                                            credentials.getAccessKey(),
                                            credentials.getSecretKey()
                                    )
                            )
                    )
                    .build();
        } catch (Exception e) {
            log.error("AWS S3 Presigner 초기화 실패: {}", e.getMessage());
            return null;
        }
    }

    public String getBucket() {
        return s3 != null ? s3.getBucket() : null;
    }
}
