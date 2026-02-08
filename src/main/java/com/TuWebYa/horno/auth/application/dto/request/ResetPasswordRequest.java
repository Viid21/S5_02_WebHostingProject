package com.TuWebYa.horno.auth.application.dto.request;

public record ResetPasswordRequest(String token, String password) {
}
