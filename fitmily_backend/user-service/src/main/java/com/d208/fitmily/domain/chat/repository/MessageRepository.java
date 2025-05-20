//package com.d208.fitmily.domain.chat.repository;
//
//import com.d208.fitmily.domain.chat.entity.ChatMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Repository;
//
//import java.util.Date;
//import java.util.List;
//
//@Slf4j
//@Repository
//public class MessageRepository {
//
//    private final MongoTemplate mongoTemplate;
//
//    @Autowired
//    public MessageRepository(MongoTemplate mongoTemplate) {
//        this.mongoTemplate = mongoTemplate;
//    }
//
//    // 메시지 저장
//    public ChatMessage save(ChatMessage message) {
//        return mongoTemplate.save(message, "messages");
//    }
//
//    // 메시지 총 개수 조회 (페이지 계산용)
//    public long countByFamilyId(String familyId) {
//        Query query = new Query(Criteria.where("familyId").is(familyId));
//        return mongoTemplate.count(query, ChatMessage.class, "messages");
//    }
//
//    // 페이지 기반 메시지 조회 - 오름차순 정렬 유지(오래된 메시지부터 최신순)
//    public List<ChatMessage> findByFamilyIdWithPagination(String familyId, int skip, int limit) {
//        Query query = new Query(Criteria.where("familyId").is(familyId))
//                .with(Sort.by(Sort.Direction.ASC, "sentAt"))
//                .skip(skip)
//                .limit(limit);
//
//        return mongoTemplate.find(query, ChatMessage.class, "messages");
//    }
//
//    // 읽음 처리 - 특정 메시지 ID 이전 메시지 일괄 처리
//    public int updateReadStatusBeforeId(String familyId, String messageId, String userId) {
//        try {
//            log.debug("메시지 일괄 읽음 처리 시작: familyId={}, messageId={}, userId={}",
//                    familyId, messageId, userId);
//
//            // 메시지 ID에서 타임스탬프 추출 (타임스탬프_familyId 형식)
//            String[] parts = messageId.split("_");
//            long timestamp = Long.parseLong(parts[0]);
//            Date messageDate = new Date(timestamp);
//
//            // 해당 시간 이전의 모든 메시지 중 아직 읽지 않은 메시지 필터링
//            Query query = new Query(Criteria.where("familyId").is(familyId)
//                    .and("sentAt").lte(messageDate)
//                    .and("readByUserIds").not().in(userId) // 아직 읽지 않은 메시지만
//                    .and("unreadCount").gt(0)); // unreadCount가 0보다 큰 경우만
//
//            Update update = new Update()
//                    .addToSet("readByUserIds", userId)
//                    .inc("unreadCount", -1);
//
//            // 일괄 업데이트
//            var updateResult = mongoTemplate.updateMulti(query, update, ChatMessage.class, "messages");
//            log.debug("메시지 일괄 읽음 처리 완료: familyId={}, messageId={}, userId={}, 업데이트 건수={}",
//                    familyId, messageId, userId, updateResult.getModifiedCount());
//
//            return (int) updateResult.getModifiedCount();
//        } catch (Exception e) {
//            log.error("메시지 일괄 읽음 처리 중 오류 발생: {}", e.getMessage(), e);
//            return 0;
//        }
//    }
//
//    // 안 읽은 메시지 수 조회
//    public long countUnreadMessages(String familyId, String userId) {
//        Query query = new Query(Criteria.where("familyId").is(familyId)
//                .and("readByUserIds").not().in(userId));
//
//        return mongoTemplate.count(query, ChatMessage.class, "messages");
//    }
//
//    // 최신 메시지 조회
//    public ChatMessage findLatestMessageByFamilyId(String familyId) {
//        Query query = new Query(Criteria.where("familyId").is(familyId))
//                .with(Sort.by(Sort.Direction.DESC, "sentAt"))
//                .limit(1);
//
//        List<ChatMessage> messages = mongoTemplate.find(query, ChatMessage.class, "messages");
//        return messages.isEmpty() ? null : messages.get(0);
//    }
//}

package com.d208.fitmily.domain.chat.repository;

import com.d208.fitmily.domain.chat.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.MongoRepository;
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

    // 페이지네이션을 이용한 메시지 조회
    public List<ChatMessage> findByFamilyIdWithPagination(String familyId, int skip, int limit) {
        Query query = new Query(Criteria.where("familyId").is(familyId))
                .skip(skip)
                .limit(limit)
                .with(org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "sentAt"));

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