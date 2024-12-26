package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    @Modifying // update를 하거나 delete 하는 쿼리는 @Modifying 붙여줘야 한다. 붙여주지 않으면 동작이 안됨
    @Query("UPDATE User u SET u.status = 'PENDING' WHERE u.status = 'APPROVED' AND u.role ='USER' AND u.id IN :userIds")
    void updatePendingStatus(List<Long> userIds);

}
