package com.d208.fitmily.config;

import com.d208.fitmily.chat.entity.ChatMessage;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.index.IndexResolver;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;

/**
 * MongoDB 통합 설정 클래스
 * 1. 기본 MongoDB 연결 설정 (MongoClient, MongoTemplate 빈 생성)
 * 2. MongoDB Repository 자동 스캔 설정
 * 3. MongoDB 인덱스 자동 생성 설정
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.d208.fitmily.chat.repository")
public class MongoDBConfig {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoDBConfig.class);

    @Value("${spring.data.mongodb.uri:mongodb://localhost:27017/fitmily}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database:fitmily}")
    private String databaseName;

    /**
     * MongoDB 클라이언트 빈 생성
     * - MongoDB 서버 연결 설정
     */
    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        log.info("MongoDB 연결 설정 완료: {}", mongoUri.replaceAll("mongodb://.*@", "mongodb://*****:*****@"));
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * MongoDB 데이터베이스 팩토리 빈 생성
     * - MongoDB 데이터베이스 연결 팩토리
     */
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), databaseName);
    }

    /**
     * MongoDB 매핑 컨텍스트 빈 생성
     * - Java 객체와 MongoDB 문서 간의 매핑 설정
     */
    @Bean
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext context = new MongoMappingContext();
        return context;
    }

    /**
     * MongoDB 템플릿 빈 생성
     * - MongoDB 데이터베이스 작업을 위한 고수준 API
     * - _class 필드 제거 설정 (선택 사항)
     */
    @Bean
    public MongoTemplate mongoTemplate() {
        // 커스텀 컨버터 설정
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDatabaseFactory()),
                mongoMappingContext()
        );

        // _class 필드 제거 (선택사항)
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory(), converter);
        log.info("MongoTemplate 생성 완료: 데이터베이스 이름 = {}", databaseName);

        return mongoTemplate;
    }

    /**
     * MongoDB 인덱스 초기화
     * - 애플리케이션 시작 시 엔티티 클래스의 @Indexed 어노테이션 기반으로 인덱스 생성
     * - PostConstruct를 통해 스프링 컨테이너 초기화 후 자동 실행
     */
    @PostConstruct
    public void initIndices() {
        try {
            log.info("MongoDB 인덱스 초기화 시작");

            // MongoDB 인덱스 자동 생성 (엔티티 클래스의 @Indexed 어노테이션 기반)
            IndexResolver resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext());

            // ChatMessage 엔티티 인덱스 생성
            IndexOperations indexOps = mongoTemplate().indexOps(ChatMessage.class);
            resolver.resolveIndexFor(ChatMessage.class).forEach(indexOps::ensureIndex);

            log.info("MongoDB 인덱스 초기화 완료");
        } catch (Exception e) {
            log.error("MongoDB 인덱스 초기화 실패: {}", e.getMessage(), e);
        }
    }
}