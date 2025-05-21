package com.d208.fitmily.global.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
@ConfigurationProperties(prefix = "spring.cloud.aws")
@Getter
@Setter
@Slf4j
public class AwsS3Config {

    private Credentials credentials;
    private RegionConfig region;  // String에서 RegionConfig로 변경
    private S3 s3;

    @Getter @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter @Setter
    public static class RegionConfig {
        private String staticRegion;  // 'static'은 Java 예약어이므로 이름 변경
        private boolean auto;

        // YAML의 'static' 속성을 'staticRegion'에 매핑
        public void setStatic(String value) {
            this.staticRegion = value;
        }

        public String getStatic() {
            return this.staticRegion;
        }
    }

    @Getter @Setter
    public static class S3 {
        private String bucket;
    }

    @PostConstruct
    public void logInitializedValues() {
        log.info("✅ AwsS3Config 초기화됨:");
        log.info("🔹 region = {}", region != null ? region.getStatic() : "null");
        log.info("🔹 region auto = {}", region != null ? region.isAuto() : "null");
        log.info("🔹 accessKey = {}", credentials != null ? credentials.getAccessKey() : "null");
        log.info("🔹 secretKey = {}", credentials != null ? "[PROTECTED]" : "null");
        log.info("🔹 bucket = {}", s3 != null ? s3.getBucket() : "null");
    }

    public S3Client s3Client() {
        if (region == null || region.getStatic() == null) {
            throw new IllegalStateException("AWS Region is not configured properly");
        }

        return S3Client.builder()
                .region(Region.of(region.getStatic()))  // staticRegion 값 사용
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(credentials.getAccessKey(), credentials.getSecretKey())
                        )
                )
                .build();
    }

    public S3Presigner s3Presigner() {
        if (region == null || region.getStatic() == null) {
            throw new IllegalStateException("AWS Region is not configured properly");
        }

        return S3Presigner.builder()
                .region(Region.of(region.getStatic()))  // staticRegion 값 사용
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(credentials.getAccessKey(), credentials.getSecretKey())
                        )
                )
                .build();
    }

    public String getBucket() {
        return s3 != null ? s3.getBucket() : null;
    }
}
