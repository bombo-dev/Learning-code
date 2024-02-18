package com.group.libraryapp.user.application;

import com.group.libraryapp.user.application.port.in.LoadUsersUseCase;
import com.group.libraryapp.user.application.port.out.LoadUserPort;
import com.group.libraryapp.user.application.port.out.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoadUserService implements LoadUsersUseCase {

    private final LoadUserPort loadUserPort;

    public LoadUserService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponse> getUsers() {
        return loadUserPort.getUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }
}
