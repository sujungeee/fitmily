package com.d208.fitmily.chat.test;

import com.d208.fitmily.chat.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class MongoDBTester implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== MongoDB 연결 테스트 시작 ===");

        try {
            // MongoDB 컬렉션 확인
            List<String> collections = mongoTemplate.getCollectionNames().stream().toList();
            System.out.println("컬렉션 목록: " + collections);

            // 테스트 메시지 생성
            String testMessageId = "test_" + System.currentTimeMillis();

            ChatMessage testMessage = new ChatMessage();
            testMessage.setMessageId(testMessageId);
            testMessage.setFamilyId("1");
            testMessage.setSenderId("3");

            ChatMessage.SenderInfo senderInfo = new ChatMessage.SenderInfo();
            senderInfo.setNickname("테스트사용자");
            senderInfo.setFamilySequence("1");
            testMessage.setSenderInfo(senderInfo);

            ChatMessage.MessageContent content = new ChatMessage.MessageContent();
            content.setMessageType("text");
            content.setText("MongoDB 연결 테스트 메시지입니다.");
            content.setImageUrl(null);
            testMessage.setContent(content);

            testMessage.setSentAt(new Date());
            testMessage.setReadByUserIds(Arrays.asList("3"));
            testMessage.setUnreadCount(1);

            // 저장
            ChatMessage saved = mongoTemplate.save(testMessage, "messages");
            System.out.println("저장된 메시지: " + saved);

            // 조회
            System.out.println("=== MongoDB 메시지 조회 테스트 ===");
            List<ChatMessage> messages = mongoTemplate.findAll(ChatMessage.class, "messages");
            System.out.println("메시지 개수: " + messages.size());
            messages.forEach(msg -> System.out.println(msg.getMessageId() + ": " + msg.getContent().getText()));

            System.out.println("=== MongoDB 연결 테스트 완료 ===");
        } catch (Exception e) {
            System.err.println("MongoDB 연결 테스트 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}