package com.TuWebYa.horno.user.application.exception;

public class UserForbiddenException extends RuntimeException {
    public UserForbiddenException() {
        super("You have no poser here.");
    }
    
    public UserForbiddenException(String message) {
        super(message);
    }
}
