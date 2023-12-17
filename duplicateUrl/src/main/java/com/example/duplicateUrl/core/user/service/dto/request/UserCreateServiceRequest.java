package com.example.duplicateUrl.core.user.service.dto.request;

import com.example.duplicateUrl.core.user.domain.User;
import lombok.Builder;

@Builder
public record UserCreateServiceRequest(
        String email,
        String name
) {

    public User toEntity() {
        return User.createUser(this.email, this.name);
    }
}
