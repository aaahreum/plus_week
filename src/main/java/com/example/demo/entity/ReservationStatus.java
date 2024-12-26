package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    PENDING,
    APPROVED,
    BLOCKED,
    EXPIRED
}
