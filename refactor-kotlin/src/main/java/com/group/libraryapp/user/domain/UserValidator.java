package com.group.libraryapp.user.domain;

public interface UserValidator {

    static void validConstructUser(String name, Integer age) {
        if (name.isBlank()) {
            throw new IllegalArgumentException();
        }

        if (age <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
