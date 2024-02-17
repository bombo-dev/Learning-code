package com.group.libraryapp.user.adapter.in.web;

import com.group.libraryapp.user.adapter.in.web.dto.UserCreateRequest;
import com.group.libraryapp.user.adapter.in.web.dto.UserUpdateRequest;
import com.group.libraryapp.user.application.port.out.dto.UserResponse;
import com.group.libraryapp.user.application.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public void saveUser(@RequestBody UserCreateRequest request) {
    userService.saveUser(request);
  }

  @GetMapping("/user")
  public List<UserResponse> getUsers() {
    return userService.getUsers();
  }

  @PutMapping("/user")
  public void updateUserName(@RequestBody UserUpdateRequest request) {
    userService.updateUserName(request);
  }

  @DeleteMapping("/user")
  public void deleteUser(@RequestParam String name) {
    userService.deleteUser(name);
  }

}
