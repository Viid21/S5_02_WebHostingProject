package com.TuWebYa.horno.user.domain.model;

import java.util.UUID;

public final class UserId {
    private final UUID value;

    private UserId(UUID value) {
        this.value = value;
    }

    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId from(String raw) {
        return new UserId(UUID.fromString(raw));
    }

    public UUID value() {
        return value;
    }
}

