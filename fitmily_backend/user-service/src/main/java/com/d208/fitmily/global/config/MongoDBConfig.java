package com.d208.fitmily.global.config;

import com.google.api.client.util.Value;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.d208.fitmily.domain.chat.repository")
public class MongoDBConfig {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MongoDBConfig.class);

    // MongoDB 컨테이너의 실제 내부 IP 주소로 업데이트
    private final String mongoHost = "172.18.0.9";
    private final String mongoPort = "27017";
    private final String mongoDatabase = "fitmily";

    @Bean
    public MongoClient mongoClient() {
        // 디버깅을 위한 상세 로그
        log.info("=== MongoDB 연결 설정 ===");
        log.info("호스트: {}", mongoHost);
        log.info("포트: {}", mongoPort);
        log.info("데이터베이스: {}", mongoDatabase);

        String mongoUri = String.format("mongodb://%s:%s/%s", mongoHost, mongoPort, mongoDatabase);
        log.info("MongoDB 연결 URI: {}", mongoUri);

        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(settings);
    }


//package com.d208.fitmily.global.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
//import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
//import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
//import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//@Configuration
//@EnableMongoRepositories(basePackages = "com.d208.fitmily.domain.chat.repository")
//public class MongoDBConfig {
//
//    private static final org.slf4j.Logger log = LoggerFactory.getLogger(MongoDBConfig.class);
//
//    // 로컬 테스트용 하드코딩 값
//    private final String mongoHost = "localhost";
//    private final String mongoPort = "27017";      // 기본 MongoDB 포트
//    private final String mongoDatabase = "fitmily";
//
//    @Bean
//    public MongoClient mongoClient() {
//        // 디버깅을 위한 상세 로그
//        log.info("=== MongoDB 연결 설정 ===");
//        log.info("호스트: {}", mongoHost);
//        log.info("포트: {}", mongoPort);
//        log.info("데이터베이스: {}", mongoDatabase);
//
//        String mongoUri = String.format("mongodb://%s:%s/%s", mongoHost, mongoPort, mongoDatabase);
//        log.info("MongoDB 연결 URI: {}", mongoUri);
//
//        ConnectionString connectionString = new ConnectionString(mongoUri);
//        MongoClientSettings settings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        return MongoClients.create(settings);
//    }

    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), mongoDatabase);
    }

    @Bean
    public MongoMappingContext mongoMappingContext() {
        return new MongoMappingContext();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(
                new DefaultDbRefResolver(mongoDatabaseFactory()), mongoMappingContext());
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));

        log.info("MongoTemplate 생성 완료: 데이터베이스 이름 = {}", mongoDatabase);

        return new MongoTemplate(mongoDatabaseFactory(), converter);
    }
}