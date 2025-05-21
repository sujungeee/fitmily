package com.d208.fitmily.domain.AwsS3.Service;

import com.d208.fitmily.domain.AwsS3.Dto.UploadUrlRequestDto;
import com.d208.fitmily.global.config.AwsS3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.*;
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

    // 업로드용
    public String generatePresignedUploadUrl(UploadUrlRequestDto dto) {
        try {
            S3Presigner presigner = awsS3Config.s3Presigner();
            if (presigner == null) {
                System.out.println("여기서 안됨");
                return null;
            }

            // 어떤 객체를 올릴지 정의
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(awsS3Config.getBucket()) // getBucket() 메서드 사용
                    .key(dto.getFilename())
                    .contentType(dto.getContentType())
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

    // 조회ro다운로드용
    public String generatePresignedDownloadUrl(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }
        if (!doesObjectExist(fileName)) {
            log.warn("❌ Presigned URL 생성 실패 - 파일이 존재하지 않음: {}", fileName);
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

    // s3에 파일 존재 여부 확인
    public boolean doesObjectExist(String fileName) {
        try {
            // 1. 존재 여부 확인 요청 객체 생성
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket(awsS3Config.getBucket())  // 조회할 S3 버킷
                    .key(fileName)                    // 조회할 객체 Key (= 파일 이름)
                    .build();

            // 2. 실제 요청 전송 (예외 없으면 존재함)
            awsS3Config.s3Client().headObject(headObjectRequest);

            return true; // 예외 없으면 파일 존재함
        } catch (S3Exception e) {
            return false; // 예외 발생 → 파일 없음 또는 접근 불가
        }
    }
}
