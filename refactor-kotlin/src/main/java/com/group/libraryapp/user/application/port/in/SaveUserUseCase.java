package com.group.libraryapp.user.application.port.in;

import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;

public interface SaveUserUseCase {

    void saveUser(UserCreateCommand command);
}
