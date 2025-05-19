package com.d208.fitmily.global.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
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

    public S3Client s3Client() {
        return S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(credentials.getAccessKey(), credentials.getSecretKey())
                        )
                )
                .build();
    }

    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(software.amazon.awssdk.regions.Region.of(region))
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