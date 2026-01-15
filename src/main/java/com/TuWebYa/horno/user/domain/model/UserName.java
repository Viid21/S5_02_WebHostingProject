package com.TuWebYa.horno.user.domain.model;

import com.TuWebYa.horno.user.domain.exception.InvalidNameException;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserName {
    private static final Pattern NAME_REGEX = Pattern.compile(
            "^[A-Za-zÁÉÍÓÚáéíóúñÑ ]+$"
    );

    private final String value;

    private UserName(String value) {
        this.value = value;
    }

    public static UserName of(String rawName){
        if (rawName == null || rawName.trim().isEmpty()) {
            throw new InvalidNameException("Name cannot be empty");
        }

        String name = rawName.trim();

        if (!NAME_REGEX.matcher(name).matches() || (name.length() < 2 || name.length() > 50)) {
            throw new InvalidNameException("Invalid name format: " + rawName);
        }

        String normalized = Arrays.stream(name.split(" "))
                .filter(part -> !part.isBlank())
                .map(part -> part.substring(0,1).toUpperCase() + part.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));

        return new UserName(normalized);
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
