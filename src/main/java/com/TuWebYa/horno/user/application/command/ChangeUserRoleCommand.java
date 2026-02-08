package com.TuWebYa.horno.user.application.command;

import java.util.UUID;

public record ChangeUserRoleCommand(
        UUID userId,
        String newRole,
        UUID authenticatedUserId,
        String authenticatedUserRole
) {
}

