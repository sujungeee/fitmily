package com.d208.fitmily.domain.chat.repository;

import com.d208.fitmily.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageRepository {

    private final MongoTemplate mongoTemplate;

    // 가족 ID로 메시지 개수 조회
    public long countByFamilyId(String familyId) {
        Query query = new Query(Criteria.where("familyId").is(familyId));
        return mongoTemplate.count(query, ChatMessage.class);
    }

    // 페이지네이션을 이용한 메시지 조회 - 최신 메시지부터 정렬
    public List<ChatMessage> findByFamilyIdWithPagination(String familyId, int skip, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId))
                .skip(skip)
                .limit(limit)
                .with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "sentAt"));

        return mongoTemplate.find(query, ChatMessage.class);
    }

    // 읽음 상태 일괄 업데이트
    public int updateReadStatusBeforeId(String familyId, String messageId, String userId) {
        try {
            // 1. 메시지 ID 조회
            ChatMessage message = mongoTemplate.findById(messageId, ChatMessage.class);
            if (message == null) {
                return 0;
            }

            // 2. 업데이트 쿼리 생성
            Query query = new Query(Criteria.where("familyId").is(familyId)
                    .and("sentAt").lte(message.getSentAt())
                    .and("readByUserIds").nin(userId));

            // 3. 업데이트 내용 설정
            Update update = new Update();
            update.addToSet("readByUserIds", userId);
            update.inc("unreadCount", -1);

            // 4. 업데이트 실행
            return (int) mongoTemplate.updateMulti(query, update, ChatMessage.class).getModifiedCount();
        } catch (Exception e) {
            return 0;
        }
    }

    // 메시지 ID로 메시지 저장
    public ChatMessage save(ChatMessage message) {
        return mongoTemplate.save(message);
    }

    // 메시지 ID로 메시지 조회
    public ChatMessage findById(String messageId) {
        return mongoTemplate.findById(messageId, ChatMessage.class);
    }
}