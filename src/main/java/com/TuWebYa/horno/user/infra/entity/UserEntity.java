package com.TuWebYa.horno.user.infra.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
@Data
@NoArgsConstructor
public class UserEntity {
    @Id
    private UUID id = UUID.randomUUID();
    private String name;
    private String email;
    private String password;
    private String role;
}
