package com.group.libraryapp.user.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaEntityRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByName(String name);
}
