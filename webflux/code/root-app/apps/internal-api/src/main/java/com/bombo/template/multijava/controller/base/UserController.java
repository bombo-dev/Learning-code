package com.bombo.template.multijava.controller.base;

import com.bombo.template.multijava.domain.user.User;
import com.bombo.template.multijava.usecase.UserUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserUseCase userUseCase;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserController(UserUseCase userUseCase) {
        this.userUseCase = userUseCase;
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("getUser");
        User user = userUseCase.getUser(id); // RestTemplate 당연히 여기는 블락
        stopWatch.stop();
        log.info("===== {} =====", stopWatch.prettyPrint());
        return ResponseEntity.ok(user);
    }
}
