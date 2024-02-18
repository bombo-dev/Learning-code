package com.group.libraryapp.user.application.port.in;

import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;

public interface SaveUserUseCase {

    Long saveUser(UserCreateCommand command);
}
