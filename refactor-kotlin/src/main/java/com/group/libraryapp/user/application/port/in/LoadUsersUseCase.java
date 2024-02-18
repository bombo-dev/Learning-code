package com.group.libraryapp.user.application.port.in;

import com.group.libraryapp.user.application.port.out.dto.UserResponse;

import java.util.List;

public interface LoadUsersUseCase {

    List<UserResponse> getUsers();
}
