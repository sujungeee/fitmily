package com.d208.fitmily.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadReceiptResponseDTO {
    @Builder.Default
    private String type = "READ_RECEIPT";
    private ReadReceiptData data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReadReceiptData {
        private String userId;
        private String messageId;
        private Date timestamp;
    }
}