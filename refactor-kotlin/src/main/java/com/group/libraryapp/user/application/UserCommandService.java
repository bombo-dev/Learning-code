package com.group.libraryapp.user.application;

import com.group.libraryapp.user.application.port.in.DeleteUserUseCase;
import com.group.libraryapp.user.application.port.in.SaveUserUseCase;
import com.group.libraryapp.user.application.port.in.UpdateNameUseCase;
import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;
import com.group.libraryapp.user.application.port.in.dto.UserUpdateCommand;
import com.group.libraryapp.user.application.port.out.LoadUserPort;
import com.group.libraryapp.user.application.port.out.UserCommandPort;
import com.group.libraryapp.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserCommandService implements SaveUserUseCase, UpdateNameUseCase, DeleteUserUseCase {

    private final UserCommandPort userCommandPort;
    private final LoadUserPort loadUserPort;

    public UserCommandService(UserCommandPort userCommandPort, LoadUserPort loadUserPort) {
        this.userCommandPort = userCommandPort;
        this.loadUserPort = loadUserPort;
    }

    @Transactional
    @Override
    public Long saveUser(UserCreateCommand command) {
        return userCommandPort.save(command);
    }

    @Transactional
    @Override
    public void updateName(UserUpdateCommand command) {
        User user = loadUserPort.getUser(command.getId());
        user.updateName(command.getName());

        userCommandPort.updateUserState(user);
    }

    @Transactional
    @Override
    public void deleteUser(String userName) {
        userCommandPort.delete(userName);
    }
}
