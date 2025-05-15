package com.d208.fitmily.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadReceiptRequestDTO {
    private String messageId;  // 마지막으로 읽은 메시지 ID
}