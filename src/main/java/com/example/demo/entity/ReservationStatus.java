package com.example.demo.entity;

import lombok.Getter;

// 상태 값을 명확하게 enum으로 관리한다.
@Getter
public enum ReservationStatus {
    PENDING,
    APPROVED,
    BLOCKED,
    EXPIRED
}
