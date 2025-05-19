package com.d208.fitmily.domain.AwsS3.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class uploadUrlRequestDto {

    private String filename;
    private String contentType;

}
