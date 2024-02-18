package com.group.libraryapp.user.adapter.out.persistence;

import com.group.libraryapp.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    User toDomain(UserJpaEntity userJpaEntity) {
        return User.createUserWithId(userJpaEntity.getId(), userJpaEntity.getName(), userJpaEntity.getAge());
    }

    UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(user.getId(), user.getName(), user.getAge());
    }
}
