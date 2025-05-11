package com.d208.fitmily.chat.dto;
// 메시지 목록 응답 DTO

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessagesResponseDTO {
    private List<ChatMessageDTO> messages;
    private boolean hasMore;
}