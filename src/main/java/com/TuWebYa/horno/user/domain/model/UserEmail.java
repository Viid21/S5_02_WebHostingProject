package com.TuWebYa.horno.user.domain.model;

import com.TuWebYa.horno.user.domain.exception.InvalidEmailException;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserEmail {
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    private final String value;
    private UserEmail(String value){
        this.value = value;
    }

    public static UserEmail of(String rawEmail) {
        if (rawEmail == null) {
            throw new InvalidEmailException("Email cannot be null");
        }

        String email = rawEmail.trim();

        if (email.isEmpty()) {
            throw new InvalidEmailException("Email cannot be empty");
        }

        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidEmailException("Invalid email format: " + rawEmail);
        }

        email = email.toLowerCase();

        return new UserEmail(email);
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        UserEmail userEmail = (UserEmail) object;
        return Objects.equals(value, userEmail.value);
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
