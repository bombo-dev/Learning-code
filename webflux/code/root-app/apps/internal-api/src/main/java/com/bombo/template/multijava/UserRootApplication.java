package com.bombo.template.multijava;

import com.bombo.template.multijava.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;

@SpringBootApplication
public class UserRootApplication {

    public static void main(String[] args) {
        System.setProperty("reactor.netty.ioWorkerCount", "1");
        SpringApplication.run(UserRootApplication.class, args);
    }

}

@Component
class SimpleRunner implements ApplicationRunner {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private URI baseUri = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .port(8081)
            .path("/api/v1/users")
            .build()
            .encode()
            .toUri();

    @Override
    public void run(ApplicationArguments args) {
        log.info("# 요청 시작 시간: {}", LocalDateTime.now());

        for (int i = 1; i <= 5; i++) {
            this.getUser((long) i)
                    .subscribe(user -> log.info("#{}: {}", LocalDateTime.now(), user.getName()));
        }
    }

    private Mono<User> getUser(Long id) {
        URI userUri = UriComponentsBuilder.fromUri(baseUri)
                .path("/{id}")
                .build()
                .expand(id)
                .encode()
                .toUri();

        return WebClient.create()
                .get()
                .uri(userUri)
                .retrieve()
                .bodyToMono(User.class);
    }
}
