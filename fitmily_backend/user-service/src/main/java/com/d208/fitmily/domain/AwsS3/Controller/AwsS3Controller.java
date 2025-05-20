package com.d208.fitmily.domain.AwsS3.Controller;

import com.d208.fitmily.domain.AwsS3.Dto.UploadUrlRequestDto;
import com.d208.fitmily.domain.AwsS3.Dto.UploadUrlResponseDto;
import com.d208.fitmily.domain.AwsS3.Service.AwsS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Tag(name = "s3 API", description = "s3 이미지/파일 업로드 및 다운로드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3")
public class AwsS3Controller {

    private final AwsS3Service awsS3Service;

    @Operation(summary = "업로드용 Presigned URL 발급")
    @PostMapping("/upload-url")
    public ResponseEntity<UploadUrlResponseDto> getPresignedUploadUrl(@RequestBody UploadUrlRequestDto dto) {
        String presignedUrl = awsS3Service.generatePresignedUploadUrl(dto);

        UploadUrlResponseDto responseDto = new UploadUrlResponseDto();
        responseDto.setUrl(presignedUrl);

        return ResponseEntity.ok(responseDto);
    }





//    @Operation(summary = "다운로드/조회용 Presigned URL 발급")
//    @GetMapping("/download-url")
//    public ResponseEntity<Map<String, Object>> getPresignedDownloadUrl(
//            @RequestParam String filename
//    ) {
//        String presignedUrl = awsS3Service.generatePresignedDownloadUrl(filename);
//        Map<String, Object> response = new HashMap<>();
//        response.put("data", presignedUrl);
//
//        return ResponseEntity.ok(response);
//    }
}
