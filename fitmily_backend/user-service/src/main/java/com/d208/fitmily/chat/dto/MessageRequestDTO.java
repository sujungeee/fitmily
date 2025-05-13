package com.d208.fitmily.chat.dto;
// 메시지 요청 DTO

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDTO {
    private String type; // "text" 또는 "image"
    private String content;
}