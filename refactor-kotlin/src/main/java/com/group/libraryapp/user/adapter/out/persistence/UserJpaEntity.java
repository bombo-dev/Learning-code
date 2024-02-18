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

    public UserJpaEntity(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
