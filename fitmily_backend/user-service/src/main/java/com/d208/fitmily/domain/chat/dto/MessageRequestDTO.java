package com.d208.fitmily.domain.chat.dto;
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
    private String messageType;
    private String content;
    private String imageUrl;
}