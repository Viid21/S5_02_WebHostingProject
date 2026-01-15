package com.TuWebYa.horno.user.domain.model;

import com.TuWebYa.horno.user.domain.exception.InvalidPasswordException;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Objects;

public class UserPassword {
    private static final int MIN_LENGTH = 8;
    private final String hashedValue;

    private UserPassword(String hashedValue) {
        this.hashedValue = hashedValue;
    }

    public static UserPassword fromPlainText(String rawPassword) {
        if (rawPassword == null || rawPassword.trim().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty");
        }

        String password = rawPassword.trim();

        if (password.length() < MIN_LENGTH) {
            throw new InvalidPasswordException("Password must be at least " + MIN_LENGTH + " characters");
        }
        if (!password.matches(".*[A-Z].*"))
            throw new InvalidPasswordException("Password must contain an uppercase letter");
        if (!password.matches(".*[a-z].*"))
            throw new InvalidPasswordException("Password must contain a lowercase letter");
        if (!password.matches(".*\\d.*")) throw new InvalidPasswordException("Password must contain a number");
        if (!password.matches(".*[@#$%^&+=!?.].*"))
            throw new InvalidPasswordException("Password must contain a special character");

        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());

        return new UserPassword(hashed);
    }

    public static UserPassword fromHashed(String hashedPassword) {
        if (hashedPassword == null || hashedPassword.trim().isEmpty()) {
            throw new InvalidPasswordException("Hashed password cannot be empty");
        }
        return new UserPassword(hashedPassword);
    }

    public boolean matches(String rawPassword) {
        return BCrypt.checkpw(rawPassword, this.hashedValue);
    }

    public String hashed() {
        return hashedValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }

    @Override
    public String toString() {
        return "***";
    }
}
