//package com.d208.fitmily.global.config;
//
//import com.d208.fitmily.domain.chat.entity.ChatMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.ContextRefreshedEvent;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.index.IndexOperations;
//import org.springframework.data.mongodb.core.index.IndexResolver;
//import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//
///**
// * MongoDB 인덱스 초기화 설정
// * - 애플리케이션 컨텍스트 로드 후 실행되어 순환 참조 문제 방지
// */
//@Configuration
//public class MongoIndexInitializer implements ApplicationListener<ContextRefreshedEvent> {
//
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoIndexInitializer.class);
//
//    private final MongoTemplate mongoTemplate;
//    private final MongoMappingContext mongoMappingContext;
//
//    @Autowired
//    public MongoIndexInitializer(MongoTemplate mongoTemplate, MongoMappingContext mongoMappingContext) {
//        this.mongoTemplate = mongoTemplate;
//        this.mongoMappingContext = mongoMappingContext;
//    }
//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        log.info("MongoDB 인덱스 초기화 시작");
//
//        try {
//            // 모든 빈이 초기화된 후 호출되므로 순환 참조 문제 없음
//            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);
//
//            // ChatMessage 엔티티 인덱스 생성
//            IndexOperations indexOps = mongoTemplate.indexOps(ChatMessage.class);
//            resolver.resolveIndexFor(ChatMessage.class).forEach(indexOps::ensureIndex);
//
//            log.info("MongoDB 인덱스 초기화 완료");
//        } catch (Exception e) {
//            log.error("MongoDB 인덱스 초기화 실패: {}", e.getMessage(), e);
//        }
//    }
//}