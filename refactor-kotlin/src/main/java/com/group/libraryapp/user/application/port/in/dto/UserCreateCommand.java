package com.group.libraryapp.user.application.port.in.dto;

import com.group.libraryapp.global.validator.SelfValidator;

import javax.validation.constraints.NotNull;

public class UserCreateCommand extends SelfValidator<UserCreateCommand> {

    @NotNull
    private final String name;

    @NotNull
    private final Integer age;

    public UserCreateCommand(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.validateSelf();
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
