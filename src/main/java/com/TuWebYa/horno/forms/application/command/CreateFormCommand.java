package com.TuWebYa.horno.forms.application.command;

import java.util.UUID;

public record CreateFormCommand(
        String email,
        String businessName,
        String businessInfo,
        String objectives,
        String preferences,
        UUID userId,
        UUID assignedAdminId
) {
}

