package com.d208.fitmily.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String messageId;
    private String senderId;
    private SenderInfoDTO senderInfo;
    private MessageContentDTO content;
    private Date sentAt;
    private List<String> readByUserIds;
    private int unreadCount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderInfoDTO {
        private String nickname;
        private String profileColor;
        private String profileImageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageContentDTO {
        private String type;
        private String text;
        private String imageUrl;
//        private List<MentionDTO> mentions;
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class MentionDTO {
//        private String userId;
//        private String nickname;
//        private int startIndex;
//        private int endIndex;
//    }
}