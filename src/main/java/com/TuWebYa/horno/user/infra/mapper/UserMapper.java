package com.TuWebYa.horno.user.infra.mapper;

import com.TuWebYa.horno.user.domain.model.*;
import com.TuWebYa.horno.user.infra.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMapper {
    public static UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();

        if (user.getId() != null) {
            entity.setId(user.getId().value());
        } else {
            entity.setId(UUID.randomUUID());
        }

        entity.setEmail(user.getEmail().value());
        entity.setPassword(user.getPassword().hashed());
        entity.setRole(user.getRole().toString());
        entity.setName(user.getName().value());

        return entity;
    }


    public static User toDomain(UserEntity entity) {
        User user = new User(
                UserEmail.of(entity.getEmail()),
                UserPassword.fromHashed(entity.getPassword()),
                UserRole.valueOf(entity.getRole())
        );
        user.setId(UserId.from(String.valueOf(entity.getId())));
        user.setName(UserName.of(entity.getName()));
        user.setRole(UserRole.valueOf(entity.getRole()));

        return user;
    }
}
