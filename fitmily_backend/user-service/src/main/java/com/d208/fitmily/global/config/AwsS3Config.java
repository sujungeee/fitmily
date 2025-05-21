package com.d208.fitmily.global.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
@Slf4j
public class AwsS3Config {

    // 직접 값 주입 방식으로 변경
    @Value("${spring.cloud.aws.credentials.accessKey:AKIAXZ2CK3QV4WQ7TVW5}")
    private String accessKey;

    @Value("${spring.cloud.aws.credentials.secretKey:Rc5fGV+0LEmKIggmAQvNKQkLTOfv/wGX2mJ92H4X}")
    private String secretKey;

    @Value("${spring.cloud.aws.region.static:us-east-1}")
    private String region;

    @Value("${spring.cloud.aws.s3.bucket:pop4u}")
    private String bucket;

    @PostConstruct
    public void logInitializedValues() {
        log.info("✅ AwsS3Config 초기화됨:");
        log.info("🔹 region = {}", region);
        log.info("🔹 accessKey = {}", accessKey);
        log.info("🔹 secretKey = [PROTECTED]");
        log.info("🔹 bucket = {}", bucket);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .build();
    }

    public String getBucket() {
        return bucket;
    }
}
