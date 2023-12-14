package com.example.duplicateUrl.core.user.service;

import com.example.duplicateUrl.core.user.domain.User;
import com.example.duplicateUrl.core.user.repository.UserRepository;
import com.example.duplicateUrl.core.user.service.dto.request.UserCreateServiceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Long save(UserCreateServiceRequest request) {
        User user = request.toEntity();
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }
}
