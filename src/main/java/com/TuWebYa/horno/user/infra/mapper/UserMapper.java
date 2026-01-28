package com.TuWebYa.horno.user.infra.mapper;

import com.TuWebYa.horno.user.domain.model.*;
import com.TuWebYa.horno.user.infra.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
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
        User user = new User(
                UserEmail.of(entity.email()),
                UserPassword.fromHashed(entity.password()),
                UserRole.valueOf(entity.role())
        );
        user.setId(UserId.from(String.valueOf(entity.id())));
        user.setName(UserName.of(entity.name()));
        user.setRole(UserRole.valueOf(entity.role()));

        return user;
    }
}
