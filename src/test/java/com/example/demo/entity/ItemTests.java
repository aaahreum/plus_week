package com.example.demo.entity;

import com.example.demo.config.QueryDslConfig;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest // JPA 관련 테스트 실행. 기본적으로 내장 H2 데이터베이스를 사용하여 테스트를 수행한다.
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 내장 DB 사용하지 않을 거라서 기본 DB로 연결하도록 설정한다.
@Import(QueryDslConfig.class)
public class ItemTests {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testStatusCannotBeNull() {
        User owner = new User("user", "test1@naver.com", "nick1", "test1234");
        User manager = new User("user", "test2@naver.com", "nick12", "test1234");
        userRepository.save(owner);
        userRepository.save(manager);

        // status 필드에 null을 삽입하려고 할 때 PersistenceException 예외가 발생하는지 확인
        assertThrows(PersistenceException.class, () -> {
            // 네이티브 SQL 쿼리 실행
            entityManager.createNativeQuery(
                    "INSERT INTO item (name, description, owner_id, manager_id, status) VALUES ('ItemName', 'ItemDescription', 1, 2, NULL)"
            ).executeUpdate();
        });

    }
}
