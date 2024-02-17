package com.group.libraryapp.user.application;

import com.group.libraryapp.user.domain.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.user.adapter.in.web.dto.UserCreateRequest;
import com.group.libraryapp.user.adapter.in.web.dto.UserUpdateRequest;
import com.group.libraryapp.user.application.port.out.dto.UserResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public void saveUser(UserCreateRequest request) {
    User newUser = User.createNewUser(request.getName(), request.getAge());
    userRepository.save(newUser);
  }

  @Transactional(readOnly = true)
  public List<UserResponse> getUsers() {
    return userRepository.findAll().stream()
        .map(UserResponse::new)
        .collect(Collectors.toList());
  }

  @Transactional
  public void updateUserName(UserUpdateRequest request) {
    User user = userRepository.findById(request.getId()).orElseThrow(IllegalArgumentException::new);
    user.updateName(request.getName());
  }

  @Transactional
  public void deleteUser(String name) {
    User user = userRepository.findByName(name).orElseThrow(IllegalArgumentException::new);
    userRepository.delete(user);
  }

}
