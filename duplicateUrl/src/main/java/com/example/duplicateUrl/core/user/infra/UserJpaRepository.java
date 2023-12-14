package com.example.duplicateUrl.core.user.infra;

import com.example.duplicateUrl.core.user.domain.User;
import com.example.duplicateUrl.core.user.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends UserRepository, JpaRepository<User, Long> {
}
