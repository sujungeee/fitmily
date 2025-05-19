package com.d208.fitmily.domain.AwsS3.Service;

import com.d208.fitmily.domain.AwsS3.Dto.UploadUrlRequestDto;
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

            // 어떤 객체를 올릴지 정의
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Config.getBucket()) // getBucket() 메서드 사용
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            // Presigned URL 설정
            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(putObjectRequest)
                    .build();

            // Presigned URL 생성
            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("업로드 URL 생성 실패: {}", e.getMessage());
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

            // 다운로드할 파일 정보
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(awsS3Config.getBucket()) // getBucket() 메서드 사용
                    .key(fileName)
                    .build();

            // Presigned GET URL 생성 설정
            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .getObjectRequest(getObjectRequest)
                    .build();

            // Presigned URL 생성
            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
            return presignedRequest.url().toString();
        } catch (Exception e) {
            log.error("다운로드 URL 생성 실패: {}", e.getMessage());
            return null;
        }
    }

    public String generatePresignedUploadUrl(UploadUrlRequestDto dto) {
        if (dto == null) {
            log.error("UploadUrlRequestDto is null");
            return null;
        }
        return generatePresignedUploadUrl(dto.getFilename(), dto.getContenttype());
    }
}
