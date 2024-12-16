package com.bombo.template.multijava.config;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer {

    private final UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        userRepository.save(User.newInstance("User A"));
        userRepository.save(User.newInstance("User B"));
    }
}
