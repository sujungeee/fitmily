package com.d208.fitmily.domain.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 채팅 메시지 엔티티
 * MongoDB messages 컬렉션에 저장
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
@CompoundIndexes({
        @CompoundIndex(name = "family_sentAt_idx", def = "{'familyId': 1, 'sentAt': -1}"),
        @CompoundIndex(name = "family_unread_idx", def = "{'familyId': 1, 'readByUserIds': 1}")
})
public class ChatMessage {

    @Id
    private String messageId;

    @Indexed
    private String familyId;

    private String senderId;
    private SenderInfo senderInfo;
    private MessageContent content;

    @Indexed
    private Date sentAt;

    private List<String> readByUserIds = new ArrayList<>();
    private int unreadCount;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderInfo {
        private String nickname;
        private String familySequence;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageContent {
        private String messageType;
        private String text;
        private String imageUrl;
    }
}