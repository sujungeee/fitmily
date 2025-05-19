package com.d208.fitmily.domain.AwsS3.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadUrlRequestDto {
    private String folder;
    private String filename;
    private String contenttype;
}

