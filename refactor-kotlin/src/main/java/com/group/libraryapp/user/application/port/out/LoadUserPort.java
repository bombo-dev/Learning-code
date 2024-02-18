package com.group.libraryapp.user.application.port.out;

import com.group.libraryapp.user.domain.User;

import java.util.List;

public interface LoadUserPort {

    User getUser(Long id);
    User getUser(String name);
    List<User> getUsers();
}
