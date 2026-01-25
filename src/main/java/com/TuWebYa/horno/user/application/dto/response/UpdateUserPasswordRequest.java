package com.TuWebYa.horno.user.application.dto.response;

public record UpdateUserPasswordRequest(String oldPassword, String newPassword) {
}
