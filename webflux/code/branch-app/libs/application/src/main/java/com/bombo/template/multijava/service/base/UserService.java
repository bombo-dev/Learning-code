package com.bombo.template.multijava.service.base;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.domain.user.UserRepository;
import com.bombo.template.multijava.usecase.UserUseCase;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserUseCase {

    private final UserRepository userClient;

    public UserService(UserRepository userClient) {
        this.userClient = userClient;
    }

    @Override
    public User getUser(Long id) {
        return new User(id, "User " + id);
    }
}
