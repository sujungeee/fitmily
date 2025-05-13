package com.d208.fitmily.chat.repository;
// MongoDB 레포지토리

import com.d208.fitmily.chat.entity.ChatMessage;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MessageRepository {

    private final MongoTemplate mongoTemplate;

    public MessageRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public ChatMessage save(ChatMessage message) {
        return mongoTemplate.save(message, "messages");
    }

    public List<ChatMessage> findByFamilyIdOrderBySentAtDesc(String familyId, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId))
                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
                .limit(limit);

        return mongoTemplate.find(query, ChatMessage.class, "messages");
    }

    public List<ChatMessage> findByFamilyIdAndSentAtBeforeOrderBySentAtDesc(String familyId, Date before, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId)
                .and("sentAt").lt(before))
                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
                .limit(limit);

        return mongoTemplate.find(query, ChatMessage.class, "messages");
    }

    public long countByFamilyIdAndSentAtBefore(String familyId, Date before) {
        Query query = new Query(Criteria.where("familyId").is(familyId)
                .and("sentAt").lt(before));

        return mongoTemplate.count(query, ChatMessage.class, "messages");
    }

    public List<ChatMessage> updateReadStatus(List<String> messageIds, String userId, String familyId) {
        // 아직 읽지 않은 메시지만 필터링
        Query query = new Query(Criteria.where("messageId").in(messageIds)
                .and("familyId").is(familyId)
                .and("readByUserIds").not().in(userId));

        Update update = new Update()
                .addToSet("readByUserIds", userId)
                .inc("unreadCount", -1);

        // 일괄 업데이트
        mongoTemplate.updateMulti(query, update, ChatMessage.class, "messages");

        // 업데이트 된 메시지 조회
        Query updatedQuery = new Query(Criteria.where("messageId").in(messageIds)
                .and("familyId").is(familyId));

        return mongoTemplate.find(updatedQuery, ChatMessage.class, "messages");
    }
}