package com.TuWebYa.horno.user.application.exception;

public class AuthIncorrectPasswordException extends RuntimeException {
    public AuthIncorrectPasswordException() {
        super("Incorrect password.");
    }
}
