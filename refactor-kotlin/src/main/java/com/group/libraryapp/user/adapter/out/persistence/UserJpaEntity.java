package com.group.libraryapp.user.adapter.out.persistence;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Integer age;

    public UserJpaEntity() {

    }

    public UserJpaEntity(String name, Integer age) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 비어 있을 수 없습니다");
        }
        this.name = name;
        this.age = age;
    }
}
