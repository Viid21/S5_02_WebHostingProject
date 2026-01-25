package com.TuWebYa.horno.user.application.command;

public record CreateUserCommand(String email, String password, String role, String authenticatedUserRole) {
}
