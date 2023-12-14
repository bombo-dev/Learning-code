package com.example.duplicateUrl.core.user.repository;

import com.example.duplicateUrl.core.user.domain.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long userId);
}
