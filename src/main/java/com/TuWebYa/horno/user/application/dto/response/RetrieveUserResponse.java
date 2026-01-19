package com.TuWebYa.horno.user.application.dto.response;

import com.TuWebYa.horno.user.domain.model.User;

public record RetrieveUserResponse(String id, String name, String email, String role) {
}
