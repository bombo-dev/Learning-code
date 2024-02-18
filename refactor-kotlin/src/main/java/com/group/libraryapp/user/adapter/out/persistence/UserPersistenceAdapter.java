package com.group.libraryapp.user.adapter.out.persistence;

import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;
import com.group.libraryapp.user.application.port.out.LoadUserPort;
import com.group.libraryapp.user.application.port.out.UserCommandPort;
import com.group.libraryapp.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPersistenceAdapter implements UserCommandPort, LoadUserPort {

    private final UserJpaEntityRepository userRepository;
    private final UserMapper userMapper;

    public UserPersistenceAdapter(UserJpaEntityRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Long save(UserCreateCommand userCreateCommand) {
        UserJpaEntity userEntity = userMapper.toEntity(userCreateCommand.toDomain());
        UserJpaEntity savedUser = userRepository.save(userEntity);
        return savedUser.getId();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDomain)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void updateUserState(User user) {
        UserJpaEntity updatedUser = userMapper.toEntity(user);
        userRepository.save(updatedUser);
    }

    @Override
    public void delete(String userName) {
        UserJpaEntity userEntity = userRepository.findByName(userName)
                .orElseThrow();

        userRepository.delete(userEntity);
    }

    @Override
    public User getUser(Long id) {
        UserJpaEntity userJpaEntity = userRepository.findById(id)
                .orElseThrow();

        return userMapper.toDomain(userJpaEntity);
    }

    @Override
    public User getUser(String name) {
        UserJpaEntity userJpaEntity = userRepository.findByName(name)
                .orElseThrow();

        return userMapper.toDomain(userJpaEntity);
    }
}
