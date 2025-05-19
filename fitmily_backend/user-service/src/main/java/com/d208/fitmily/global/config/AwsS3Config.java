package com.d208.fitmily.global.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
    private String regionStatic;
    private S3 s3;
    private Stack stack;

    // 초기화된 인스턴스 저장
    private S3Client s3ClientInstance;
    private S3Presigner s3PresignerInstance;

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

    @PostConstruct
    public void init() {
        try {
            // 실제 사용할 리전 결정 (regionStatic이 우선)
            String effectiveRegion = regionStatic != null ? regionStatic : region;

            log.info("AWS S3 설정 초기화 - 리전: {}", effectiveRegion);

            // 안전한 null 체크
            if (credentials == null || credentials.getAccessKey() == null ||
                    credentials.getSecretKey() == null || effectiveRegion == null) {
                log.warn("AWS 자격 증명이 올바르게 구성되지 않았습니다. S3 기능이 제한됩니다.");
                return; // 클라이언트 초기화 없이 종료
            }

            // S3 클라이언트 초기화
            s3ClientInstance = S3Client.builder()
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

            s3PresignerInstance = S3Presigner.builder()
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

            log.info("AWS S3 설정이 성공적으로 초기화되었습니다");
        } catch (Exception e) {
            log.error("AWS S3 설정 초기화 오류: {}", e.getMessage());
            // 예외를 다시 발생시키지 않고 애플리케이션 시작을 허용
        }
    }

    public S3Client s3Client() {
        if (s3ClientInstance == null) {
            log.warn("S3Client가 초기화되지 않았습니다");
        }
        return s3ClientInstance;
    }

    public S3Presigner s3Presigner() {
        if (s3PresignerInstance == null) {
            log.warn("S3Presigner가 초기화되지 않았습니다");
        }
        return s3PresignerInstance;
    }

    public String getBucket() {
        if (s3 == null) {
            log.warn("S3 버킷 설정이 누락되었습니다");
            return "unknown-bucket";
        }
        return s3.getBucket();
    }
}
