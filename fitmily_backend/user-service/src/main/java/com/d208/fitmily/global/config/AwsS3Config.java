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

@Component // ✅ 추가
@ConfigurationProperties(prefix = "spring.cloud.aws")
@Getter
@Setter
@Slf4j
public class AwsS3Config {

    private Credentials credentials;
    private String region;
    private S3 s3;

    @Getter @Setter
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }

    @Getter @Setter
    public static class S3 {
        private String bucket;
    }


    @PostConstruct
    public void logInitializedValues() {
        log.info("✅ AwsS3Config 초기화됨:");
        log.info("🔹 region = {}", region);
        log.info("🔹 accessKey = {}", credentials != null ? credentials.getAccessKey() : "null");
        log.info("🔹 secretKey = {}", credentials != null ? "[PROTECTED]" : "null");
        log.info("🔹 bucket = {}", s3 != null ? s3.getBucket() : "null");
    }

    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(credentials.getAccessKey(), credentials.getSecretKey())
                        )
                )
                .build();
    }

    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(credentials.getAccessKey(), credentials.getSecretKey())
                        )
                )
                .build();
    }

    public String getBucket() {
        return s3.getBucket();
    }
}
