package com.TuWebYa.horno.common.web;

import com.TuWebYa.horno.auth.application.exception.InvalidCredentialsException;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.domain.exception.InvalidEmailException;
import com.TuWebYa.horno.user.domain.exception.InvalidNameException;
import com.TuWebYa.horno.user.domain.exception.InvalidPasswordException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidEmailException.class)
    public Mono<ResponseEntity<String>> handleInvalidEmail(InvalidEmailException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public Mono<ResponseEntity<String>> handleInvalidPassword(InvalidPasswordException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidNameException.class)
    public Mono<ResponseEntity<String>> handleInvalidName(InvalidNameException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<String>> handleUserNotFound(UserNotFoundException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(UserForbiddenException.class)
    public Mono<ResponseEntity<String>> handleUserForbidden(UserForbiddenException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public Mono<ResponseEntity<String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }
}
