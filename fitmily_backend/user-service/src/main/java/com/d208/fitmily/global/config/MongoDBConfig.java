<<<<<<< HEAD
//package com.d208.fitmily.global.config;
//
//import com.mongodb.ConnectionString;
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.MongoClients;
//import org.springframework.beans.factory.annotation.Value;
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
//@EnableMongoRepositories(basePackages = "com.d208.fitmily.chat.repository")
//public class MongoDBConfig {
//
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoDBConfig.class);
//
//    @Value("${spring.data.mongodb.uri:mongodb://mongodb:27017/fitmily}")
//    private String mongoUri;
//
//    @Value("${spring.data.mongodb.database:fitmily}")
//    private String databaseName;
//
//    /**
//     * MongoDB 클라이언트 빈 생성
//     */
//    @Bean
//    public MongoClient mongoClient() {
//        ConnectionString connectionString = new ConnectionString(mongoUri);
//        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
//                .applyConnectionString(connectionString)
//                .build();
//
//        log.info("MongoDB 연결 설정 완료: {}", mongoUri.replaceAll("mongodb://.*@", "mongodb://*****:*****@"));
//        return MongoClients.create(mongoClientSettings);
//    }
//
//    /**
//     * MongoDB 데이터베이스 팩토리 빈 생성
//     */
//    @Bean
//    public MongoDatabaseFactory mongoDatabaseFactory() {
//        return new SimpleMongoClientDatabaseFactory(mongoClient(), databaseName);
//    }
//
//    /**
//     * MongoDB 매핑 컨텍스트 빈 생성
//     */
//    @Bean
//    public MongoMappingContext mongoMappingContext() {
//        MongoMappingContext context = new MongoMappingContext();
//        return context;
//    }
//
//    /**
//     * MongoDB 템플릿 빈 생성
//     */
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        // 커스텀 컨버터 설정
//        MappingMongoConverter converter = new MappingMongoConverter(
//                new DefaultDbRefResolver(mongoDatabaseFactory()),
//                mongoMappingContext()
//        );
//
//        // _class 필드 제거 (선택사항)
//        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
//
//        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory(), converter);
//        log.info("MongoTemplate 생성 완료: 데이터베이스 이름 = {}", databaseName);
//
//        return mongoTemplate;
//    }
//}
=======
package com.d208.fitmily.global.config;

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
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.d208.fitmily.domain.chat.repository")
public class MongoDBConfig {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(MongoDBConfig.class);

    @Value("${spring.data.mongodb.host:mongodb}")
    private String mongoHost;

    @Value("${spring.data.mongodb.port:27017}")
    private String mongoPort;

    @Value("${spring.data.mongodb.database:fitmily}")
    private String databaseName;

    /**
     * MongoDB 클라이언트 빈 생성
     */
    @Bean
    public MongoClient mongoClient() {
        // host와 port를 명시적으로 사용하는 방식으로 구성
        String mongoUri = String.format("mongodb://%s:%s/%s", mongoHost, mongoPort, databaseName);
        ConnectionString connectionString = new ConnectionString(mongoUri);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        log.info("MongoDB 연결 설정 완료: {}", mongoUri);
        return MongoClients.create(mongoClientSettings);
    }

    /**
     * MongoDB 데이터베이스 팩토리 빈 생성
     */
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new SimpleMongoClientDatabaseFactory(mongoClient(), databaseName);
    }

    /**
     * MongoDB 매핑 컨텍스트 빈 생성
     */
    @Bean
    public MongoMappingContext mongoMappingContext() {
        MongoMappingContext context = new MongoMappingContext();
        return context;
    }

    /**
     * MongoDB 템플릿 빈 생성
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
}
>>>>>>> family
