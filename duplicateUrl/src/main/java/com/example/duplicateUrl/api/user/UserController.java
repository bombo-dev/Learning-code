package com.example.duplicateUrl.api.user;

import com.example.duplicateUrl.api.global.response.ApiResponse;
import com.example.duplicateUrl.api.user.dto.request.UserCreateRequest;
import com.example.duplicateUrl.core.global.config.Duplicate;
import com.example.duplicateUrl.core.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;


    @Duplicate
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> save(HttpServletRequest request, @RequestBody UserCreateRequest userCreateRequest) {
        Long savedId = userService.save(userCreateRequest.toService());

        return ResponseEntity.created(URI.create("/api/v1/users/" + savedId))
                .body(ApiResponse.ok(savedId));
    }
}
