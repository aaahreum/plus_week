package com.example.demo.repository;

import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom {

    // QueryDSL을 사용하기 위해 JPAQueryFactory 주입
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Reservation> searchReservations(Long userId, Long itemId) {
        QReservation reservation = QReservation.reservation;
        QUser user = QUser.user;
        QItem item = QItem.item;

        return queryFactory
                .selectFrom(reservation)
                .join(reservation.user, user).fetchJoin()
                .join(reservation.item, item).fetchJoin()
                .where(
                        eqUserId(userId),
                        eqItemId(itemId)
                )
                .fetch();
    }

    // userId가 null이 아니라면 userId와 같은 값을 가진 예약을 찾는 조건을 추가하는 메서드
    // BooleanExpression -> QueryDSL에서 사용되는 반환 타입으로 조건을 표현하는 객체. 주로 where 절에 사용되는 조건을 동적으로 생성하는 데 사용된다.
    private BooleanExpression eqUserId(Long userId) {
        return userId != null ? QReservation.reservation.user.id.eq(userId) : null;
    }

    // itemId가 null이 아니라면 userId와 같은 값을 가진 예약을 찾는 조건을 추가하는 메서드
    private BooleanExpression eqItemId(Long itemId) {
        return itemId != null ? QReservation.reservation.item.id.eq(itemId) : null;
    }
}
