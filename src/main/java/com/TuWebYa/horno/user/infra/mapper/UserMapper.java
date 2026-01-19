package com.TuWebYa.horno.user.infra.mapper;

import com.TuWebYa.horno.user.domain.model.*;
import com.TuWebYa.horno.user.infra.entity.UserEntity;

public class UserMapper {
    public static UserEntity toEntity(User user){
        return new UserEntity(
                user.getId().value(),
                user.getName().value(),
                user.getEmail().value(),
                user.getPassword().hashed(),
                user.getRole().name()
        );
    }

    public static User toDomain(UserEntity entity) {
        return new User(
                UserId.from(String.valueOf(entity.id())),
                UserName.of(entity.name()),
                UserEmail.of(entity.email()),
                UserPassword.fromHashed(entity.password()),
                UserRole.valueOf(entity.role())
        );
    }
}
