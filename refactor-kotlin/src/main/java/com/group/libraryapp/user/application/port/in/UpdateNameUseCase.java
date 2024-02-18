package com.group.libraryapp.user.application.port.in;

import com.group.libraryapp.user.application.port.in.dto.UserUpdateCommand;

public interface UpdateNameUseCase {

    void updateName(UserUpdateCommand command);
}
