package com.example.demo.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// QueryDSL을 사용하기 위한 설정 클래스
// @Configuration-> Spring이 자동으로 JPAQueryFactory를 만들어 관리하도록 설정
@Configuration
public class QueryDslConfig {

    // @PersistenceContext-> EntityManager를 주입 받기 위해 사용한다.
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
