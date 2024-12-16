package com.bombo.template.multijava.service.base;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.domain.user.UserHttp;
import com.bombo.template.multijava.usecase.UserUseCase;
import org.springframework.stereotype.Service;

@Service
public class BaseService implements UserUseCase {

    private final UserHttp userHttp;

    public BaseService(UserHttp userHttp) {
        this.userHttp = userHttp;
    }

    @Override
    public User getUser(Long id) {
        return userHttp.getUser(id);
    }
}
