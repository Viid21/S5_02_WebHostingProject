package com.TuWebYa.horno.auth.application.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid credentials.");
    }
}
