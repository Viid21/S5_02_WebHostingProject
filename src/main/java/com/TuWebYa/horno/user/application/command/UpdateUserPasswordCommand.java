package com.TuWebYa.horno.user.application.command;

import java.util.UUID;

public record UpdateUserPasswordCommand(
        UUID id,
        String oldPassword,
        String newPassword,
        UUID authenticatedUserId,
        String authenticatedUserRole) {
}
