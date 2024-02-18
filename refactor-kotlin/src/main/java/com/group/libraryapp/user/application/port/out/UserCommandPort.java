package com.group.libraryapp.user.application.port.out;

import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;
import com.group.libraryapp.user.domain.User;

public interface UserCommandPort {

    Long save(UserCreateCommand userCreateCommand);
    void updateUserState(User user);
    void delete(String userName);
}
