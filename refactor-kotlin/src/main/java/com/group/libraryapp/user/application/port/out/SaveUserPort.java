package com.group.libraryapp.user.application.port.out;

import com.group.libraryapp.user.domain.User;

public interface SaveUserPort {

    void saveUser(User user);
}
