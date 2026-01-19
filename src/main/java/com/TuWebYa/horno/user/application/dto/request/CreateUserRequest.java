package com.TuWebYa.horno.user.application.dto.request;

public record CreateUserRequest(String id, String name, String password, String email, String role) {
}
