package com.group.libraryapp.user.adapter.in.web.dto;

import com.group.libraryapp.user.application.port.in.dto.UserCreateCommand;

public class UserCreateRequest {

  private String name;
  private Integer age;

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

  public UserCreateCommand toCommand() {
    return new UserCreateCommand(name, age);
  }
}
