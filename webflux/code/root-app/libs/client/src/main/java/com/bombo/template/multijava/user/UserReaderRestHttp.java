package com.bombo.template.multijava.user;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.domain.user.UserHttp;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class UserReaderRestHttp implements UserHttp {

    private final RestTemplate restTemplate;

    public UserReaderRestHttp() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public User getUser(Long id) {
        URI getUserUri = UriComponentsBuilder.fromUri(getBaseUrl())
                .path("/{id}")
                .build()
                .expand(id)
                .encode()
                .toUri();

        return restTemplate.getForEntity(getUserUri, User.class).getBody();
    }

    private URI getBaseUrl() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8081)
                .path("/api/v1/users")
                .build()
                .encode()
                .toUri();
    }
}
