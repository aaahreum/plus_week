package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;


@Entity
@Getter
// TODO: 6. Dynamic Insert
// @DynamicInsert -> 데이터베이스에 데이터를 저장할 때 일부 컬럼만 동적으로 삽입하게 해주는 기능
@DynamicInsert
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
    private String status;

    public Item(String name, String description, User manager, User owner) {
        this.name = name;
        this.description = description;
        this.manager = manager;
        this.owner = owner;
    }

    public Item() {}
}
