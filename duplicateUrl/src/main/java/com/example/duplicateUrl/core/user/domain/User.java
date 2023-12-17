package com.example.duplicateUrl.core.user.domain;

import com.example.duplicateUrl.core.global.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "users")
@Entity
public class User extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name", nullable = false)
    private String name;

    protected User () {

    }

    @Builder
    private User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public static User createUser(String email, String name) {
        return User.builder()
                .email(email)
                .name(name)
                .build();
    }
}
