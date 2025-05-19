package com.d208.fitmily.domain.chat.repository;

import com.d208.fitmily.domain.chat.entity.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class MessageRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MessageRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * 채팅 메시지 저장
     */
    public ChatMessage save(ChatMessage message) {
        return mongoTemplate.save(message, "messages");
    }

    /**
     * 최신 메시지 순으로 조회
     */
    public List<ChatMessage> findByFamilyIdOrderBySentAtDesc(String familyId, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId))
                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
                .limit(limit);

        return mongoTemplate.find(query, ChatMessage.class, "messages");
    }

    /**
     * 특정 시간 이전 메시지 조회 (페이징)
     */
    public List<ChatMessage> findByFamilyIdAndSentAtBeforeOrderBySentAtDesc(String familyId, Date before, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId)
                .and("sentAt").lt(before))
                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
                .limit(limit);

        return mongoTemplate.find(query, ChatMessage.class, "messages");
    }

    /**
     * 특정 시간 이전 메시지 개수 조회 (더 많은 메시지 있는지 확인용)
     */
    public long countByFamilyIdAndSentAtBefore(String familyId, Date before) {
        Query query = new Query(Criteria.where("familyId").is(familyId)
                .and("sentAt").lt(before));

        return mongoTemplate.count(query, ChatMessage.class, "messages");
    }

    /**
     * 메시지 읽음 상태 업데이트 (특정 메시지 목록)
     */
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
        log.debug("메시지 읽음 상태 업데이트: userId={}, familyId={}", userId, familyId);

        // 업데이트 된 메시지 조회
        Query updatedQuery = new Query(Criteria.where("messageId").in(messageIds)
                .and("familyId").is(familyId));

        return mongoTemplate.find(updatedQuery, ChatMessage.class, "messages");
    }

    /**
     * 특정 메시지 ID 이전의 모든 메시지 읽음 처리
     * @return 업데이트된 메시지 수
     */
    public int updateReadStatusBeforeId(String familyId, String messageId, String userId) {
        try {
            // 메시지 ID에서 타임스탬프 추출 (메시지 ID 형식: 타임스탬프_familyId)
            String[] parts = messageId.split("_");
            long timestamp = Long.parseLong(parts[0]);
            Date messageDate = new Date(timestamp);

            // 해당 시간 이전의 모든 메시지 중 아직 읽지 않은 메시지 필터링
            Query query = new Query(Criteria.where("familyId").is(familyId)
                    .and("sentAt").lte(messageDate)
                    .and("readByUserIds").not().in(userId));

            Update update = new Update()
                    .addToSet("readByUserIds", userId)
                    .inc("unreadCount", -1);

            // 일괄 업데이트
            var updateResult = mongoTemplate.updateMulti(query, update, ChatMessage.class, "messages");
            log.debug("메시지 ID 이전 일괄 읽음 처리: familyId={}, messageId={}, userId={}, 업데이트 건수={}",
                    familyId, messageId, userId, updateResult.getModifiedCount());

            return (int) updateResult.getModifiedCount();

        } catch (Exception e) {
            log.error("메시지 ID에서 타임스탬프 추출 실패, 메시지 ID 자체로 쿼리: {}", e.getMessage());

            // 타임스탬프 추출 실패 시 메시지 ID 자체로 쿼리
            Query query = new Query(Criteria.where("familyId").is(familyId)
                    .and("messageId").lte(messageId)
                    .and("readByUserIds").not().in(userId));

            Update update = new Update()
                    .addToSet("readByUserIds", userId)
                    .inc("unreadCount", -1);

            var updateResult = mongoTemplate.updateMulti(query, update, ChatMessage.class, "messages");
            log.debug("메시지 ID 기준 일괄 읽음 처리(대체 방식): familyId={}, messageId={}, userId={}, 업데이트 건수={}",
                    familyId, messageId, userId, updateResult.getModifiedCount());

            return (int) updateResult.getModifiedCount();
        }
    }

    /**
     * 특정 가족의 특정 사용자가 읽지 않은 메시지 개수 조회
     */
    public long countUnreadMessages(String familyId, String userId) {
        Query query = new Query(Criteria.where("familyId").is(familyId)
                .and("readByUserIds").not().in(userId));

        return mongoTemplate.count(query, ChatMessage.class, "messages");
    }

    /**
     * 특정 가족의 최신 메시지 조회
     */
    public ChatMessage findLatestMessageByFamilyId(String familyId) {
        Query query = new Query(Criteria.where("familyId").is(familyId))
                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
                .limit(1);

        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "messages");
        return messages.isEmpty() ? null : messages.get(0);
    }
}