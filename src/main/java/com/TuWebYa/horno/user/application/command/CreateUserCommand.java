package com.TuWebYa.horno.user.application.command;

public record CreateUserCommand(String name, String password, String email, String role) {
}
