package com.bombo.template.multijava.service.base;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.domain.user.UserHttp;
import com.bombo.template.multijava.usecase.UserUseCase;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

@Service
public class BaseService implements UserUseCase {

    private final UserHttp userHttp;

    public BaseService(UserHttp userHttp) {
        this.userHttp = userHttp;
    }

    @Override
    public User getUser(Long id) {
        StopWatch stopWatch = new StopWatch("getUserInternal");
        stopWatch.start();
        User user = userHttp.getUser(id);
        stopWatch.stop();

        System.out.println("===== " + stopWatch.prettyPrint() + " =====");

        return user;
    }
}
