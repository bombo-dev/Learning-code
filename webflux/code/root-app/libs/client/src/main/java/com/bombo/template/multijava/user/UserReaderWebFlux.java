package com.bombo.template.multijava.user;

import com.bombo.template.multijava.domain.user.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Primary
@Component
public class UserReaderWebFlux {

    private final WebClient webClient;

    public UserReaderWebFlux(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public User getUser(Long id) {
        return null;
    }
}
