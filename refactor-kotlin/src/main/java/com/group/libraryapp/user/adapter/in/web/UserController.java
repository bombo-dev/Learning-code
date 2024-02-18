package com.group.libraryapp.user.adapter.in.web;

import com.group.libraryapp.user.adapter.in.web.dto.UserCreateRequest;
import com.group.libraryapp.user.adapter.in.web.dto.UserUpdateRequest;
import com.group.libraryapp.user.application.port.in.DeleteUserUseCase;
import com.group.libraryapp.user.application.port.in.LoadUsersUseCase;
import com.group.libraryapp.user.application.port.in.SaveUserUseCase;
import com.group.libraryapp.user.application.port.in.UpdateNameUseCase;
import com.group.libraryapp.user.application.port.out.dto.UserResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

  private final LoadUsersUseCase loadUsersUseCase;
  private final UpdateNameUseCase updateNameUseCase;
  private final DeleteUserUseCase deleteUserUseCase;
  private final SaveUserUseCase saveUserUseCase;

  public UserController(LoadUsersUseCase loadUsersUseCase,
                        UpdateNameUseCase updateNameUseCase,
                        DeleteUserUseCase deleteUserUseCase,
                        SaveUserUseCase saveUserUseCase
  ) {
    this.loadUsersUseCase = loadUsersUseCase;
    this.updateNameUseCase = updateNameUseCase;
    this.deleteUserUseCase = deleteUserUseCase;
    this.saveUserUseCase = saveUserUseCase;
  }

  @PostMapping("/user")
  public void saveUser(@RequestBody UserCreateRequest request) {
    saveUserUseCase.saveUser(request.toCommand());
  }

  @GetMapping("/user")
  public List<UserResponse> getUsers() {
    return loadUsersUseCase.getUsers();
  }

  @PutMapping("/user")
  public void updateUserName(@RequestBody UserUpdateRequest request) {
    updateNameUseCase.updateName(request.toCommand());
  }

  @DeleteMapping("/user")
  public void deleteUser(@RequestParam String name) {
    deleteUserUseCase.deleteUser(name);
  }

}
