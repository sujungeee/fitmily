package com.d208.fitmily.domain.walk.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class routeImgRequest {
    private String filename;
    private String contentType;

}