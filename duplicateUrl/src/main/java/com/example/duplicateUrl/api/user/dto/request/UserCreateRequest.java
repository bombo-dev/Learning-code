package com.example.duplicateUrl.api.user.dto.request;

import com.example.duplicateUrl.core.user.service.dto.request.UserCreateServiceRequest;

public record UserCreateRequest(
        String email,
        String name
) {

    public UserCreateServiceRequest toService() {
        return UserCreateServiceRequest.builder()
                .email(email)
                .name(name)
                .build();
    }
}
