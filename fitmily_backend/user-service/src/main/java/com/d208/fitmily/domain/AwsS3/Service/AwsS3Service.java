package com.d208.fitmily.domain.AwsS3.Service;

import com.d208.fitmily.global.config.AwsS3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwsS3Service {

    private final AwsS3Config awsS3Config;

    public String generatePresignedUploadUrl(String fileName, String contentType) {
        try {
            S3Presigner presigner = awsS3Config.s3Presigner();
            if (presigner == null) {
                log.error("S3Presigner가 초기화되지 않았습니다");
                return null;
            }

            // 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Config.getBucket())
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Presigned URL 생성
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("업로드 URL 생성 오류: {}", e.getMessage());
            return null;
        }
    }

    public String generatePresignedDownloadUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }

        try {
            S3Presigner presigner = awsS3Config.s3Presigner();
            if (presigner == null) {
                log.error("S3Presigner가 초기화되지 않았습니다");
                return null;
            }

            // 다운로드 요청 생성
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsS3Config.getBucket())
                    .key(fileName)
                    .build();

            // Presigned URL 생성
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("다운로드 URL 생성 오류: {}", e.getMessage());
            return null;
        }
    }
}
