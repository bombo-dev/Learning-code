package com.group.libraryapp.user.application.port.in.dto;

import com.group.libraryapp.global.validator.SelfValidator;

import javax.validation.constraints.NotNull;

public class UserUpdateCommand extends SelfValidator<UserUpdateCommand> {

    @NotNull
    private final Long id;

    @NotNull
    private final String name;

    @NotNull
    private final Integer age;

    public UserUpdateCommand(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.validateSelf();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
