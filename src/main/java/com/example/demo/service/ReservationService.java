package com.example.demo.service;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.exception.ReservationConflictException;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RentalLogService rentalLogService;

    public ReservationService(ReservationRepository reservationRepository,
                              ItemRepository itemRepository,
                              UserRepository userRepository,
                              RentalLogService rentalLogService) {
        this.reservationRepository = reservationRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.rentalLogService = rentalLogService;
    }

    // TODO: 1. 트랜잭션 이해
    // @Transactional -> 하나의 작업이라도 실패하면 모든 작업을 롤백하여 데이터 일관성을 유지한다.
    @Transactional
    public void createReservation(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        // 쉽게 데이터를 생성하려면 아래 유효성검사 주석 처리
        List<Reservation> haveReservations = reservationRepository.findConflictingReservations(itemId, startAt, endAt);
        if (!haveReservations.isEmpty()) {
            throw new ReservationConflictException("해당 물건은 이미 그 시간에 예약이 있습니다.");
        }

        Item item = itemRepository.findItemById(itemId);
        User user = userRepository.findUserById(itemId);
        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);
        Reservation savedReservation = reservationRepository.save(reservation);

        RentalLog rentalLog = new RentalLog(savedReservation, "로그 메세지", "CREATE");
        rentalLogService.save(rentalLog);
    }

    // TODO: 3. N+1 문제
    public List<ReservationResponseDto> getReservations() {
        List<Reservation> reservations = reservationRepository.findAllWithUserAndItem();

        return reservations.stream().map(reservation -> {
            User user = reservation.getUser();
            Item item = reservation.getItem();

            return new ReservationResponseDto(
                    reservation.getId(),
                    user.getNickname(),
                    item.getName(),
                    reservation.getStatus(),
                    reservation.getStartAt(),
                    reservation.getEndAt()
            );
        }).toList();
    }

    // TODO: 5. QueryDSL 검색 개선
    public List<ReservationResponseDto> searchAndConvertReservations(Long userId, Long itemId) {

//        List<Reservation> reservations = searchReservations(userId, itemId);

        // 커스텀 레파지토리에서 정의한 searchReservations 메서드를 호출
        List<Reservation> reservations = reservationRepository.searchReservations(userId, itemId);

        return convertToDto(reservations);
    }

//    public List<Reservation> searchReservations(Long userId, Long itemId) {
//
//        if (userId != null && itemId != null) {
//            return reservationRepository.findByUserIdAndItemId(userId, itemId);
//        } else if (userId != null) {
//            return reservationRepository.findByUserId(userId);
//        } else if (itemId != null) {
//            return reservationRepository.findByItemId(itemId);
//        } else {
//            return reservationRepository.findAll();
//        }
//    }

    private List<ReservationResponseDto> convertToDto(List<Reservation> reservations) {
        return reservations.stream()
                .map(reservation -> new ReservationResponseDto(
                        reservation.getId(),
                        reservation.getUser().getNickname(),
                        reservation.getItem().getName(),
                        reservation.getStatus(),
                        reservation.getStartAt(),
                        reservation.getEndAt()
                ))
                .toList();
    }

    // TODO: 7. 리팩토링
    @Transactional
    public ReservationResponseDto updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findReservationById(reservationId);

        // 불필요한 else 구문 제거
        if ("APPROVED".equals(status) && !"PENDING".equals(reservation.getStatus())) {
            throw new IllegalArgumentException("PENDING 상태만 APPROVED로 변경 가능합니다.");
        }

        if ("CANCELED".equals(status) && "EXPIRED".equals(reservation.getStatus())) {
            throw new IllegalArgumentException("EXPIRED 상태인 예약은 취소할 수 없습니다.");
        }

        if ("EXPIRED".equals(status) && !"PENDING".equals(reservation.getStatus())) {
            throw new IllegalArgumentException("PENDING 상태만 EXPIRED로 변경 가능합니다.");
        }

        reservation.updateStatus(status);

        return new ReservationResponseDto(reservation.getId(),
                reservation.getUser().getNickname(),
                reservation.getItem().getName(),
                reservation.getStatus(),
                reservation.getStartAt(),
                reservation.getEndAt());

//        if ("APPROVED".equals(status)) {
//            if (!"PENDING".equals(reservation.getStatus())) {
//                throw new IllegalArgumentException("PENDING 상태만 APPROVED로 변경 가능합니다.");
//            }
//            reservation.updateStatus("APPROVED");
//        } else if ("CANCELED".equals(status)) {
//            if ("EXPIRED".equals(reservation.getStatus())) {
//                throw new IllegalArgumentException("EXPIRED 상태인 예약은 취소할 수 없습니다.");
//            }
//            reservation.updateStatus("CANCELED");
//        } else if ("EXPIRED".equals(status)) {
//            if (!"PENDING".equals(reservation.getStatus())) {
//                throw new IllegalArgumentException("PENDING 상태만 EXPIRED로 변경 가능합니다.");
//            }
//            reservation.updateStatus("EXPIRED");
//        } else {
//            throw new IllegalArgumentException("올바르지 않은 상태: " + status);
//        }
    }
}
