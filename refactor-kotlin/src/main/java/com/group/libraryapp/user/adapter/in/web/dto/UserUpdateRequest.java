package com.group.libraryapp.user.adapter.in.web.dto;

import com.group.libraryapp.user.application.port.in.dto.UserUpdateCommand;

public class UserUpdateRequest {

  private Long id;
  private String name;
  private Integer age;

  public UserUpdateRequest(Long id, String name, Integer age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Integer getAge() {
    return age;
  }

  public UserUpdateCommand toCommand() {
    return new UserUpdateCommand(id, name, age);
  }
}
