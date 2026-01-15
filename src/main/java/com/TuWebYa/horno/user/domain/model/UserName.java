package com.TuWebYa.horno.user.domain.model;

import com.TuWebYa.horno.user.domain.exception.InvalidEmailException;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserName {
    private static final Pattern NAME_REGEX = Pattern.compile(
            "^[A-Za-z+_.-]+$"
    );

    private final String value;

    private UserName(String value) {
        this.value = value;
    }

    public static UserName of(String rawName){
        if (rawName == null || rawName.trim().isEmpty()) {
            throw new InvalidEmailException("Name cannot be empty");
        }

        String name = rawName.trim();

        if (!NAME_REGEX.matcher(name).matches()) {
            throw new InvalidEmailException("Invalid name format: " + rawName);
        }

        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);

        return new UserName(capitalized);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        UserName userName = (UserName) object;
        return Objects.equals(value, userName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
