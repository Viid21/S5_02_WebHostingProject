package com.TuWebYa.horno.user.domain.model;

import lombok.*;

import java.util.Objects;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class User {
    private UserId id;
    private UserName name;
    @NonNull
    private UserEmail email;
    @NonNull
    private UserPassword password;
    private UserRole role;

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
