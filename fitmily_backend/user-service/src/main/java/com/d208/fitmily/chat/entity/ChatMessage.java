package com.d208.fitmily.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class ChatMessage {
    @Id
    private String id;
    private String messageId;
    private String senderId;
    private SenderInfo senderInfo;
    private MessageContent content;
    private Date sentAt;
    private List<String> readByUserIds;
    private int unreadCount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderInfo {
        private String nickname;
        private String profileColor;
        private String profileImageUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageContent {
        private String type;
        private String text;
        private String imageUrl;
//        private List<Mention> mentions;
    }

//    @Data
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class Mention {
//        private String userId;
//        private String nickname;
//        private int startIndex;
//        private int endIndex;
//    }
}