package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
// ReservationRepositoryCustom 인터페이스 추가
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom {

    // JOIN FETCH를 이용하여 Reservation 엔티티와 연관된 user, item 엔티티를 한번의 쿼리로 조회한다.
    @Query("SELECT r FROM Reservation r JOIN FETCH r.user JOIN FETCH r.item")
    List<Reservation> findAllWithUserAndItem();

//    List<Reservation> findByUserIdAndItemId(Long userId, Long itemId);
//
//    List<Reservation> findByUserId(Long userId);
//
//    List<Reservation> findByItemId(Long itemId);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.item.id = :id " +
            "AND NOT (r.endAt <= :startAt OR r.startAt >= :endAt) " +
            "AND r.status = 'APPROVED'")
    List<Reservation> findConflictingReservations(
            @Param("id") Long id,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    // default 메서드를 사용하여 중복 코드 최소화
    default Reservation findReservationById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 데이터가 존재하지 않습니다."));
    }
}
