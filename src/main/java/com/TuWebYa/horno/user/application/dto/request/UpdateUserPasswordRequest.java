package com.TuWebYa.horno.user.application.dto.request;

public record UpdateUserPasswordRequest(String oldPassword, String newPassword) {
}
