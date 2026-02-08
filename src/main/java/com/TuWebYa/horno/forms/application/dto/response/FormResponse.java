package com.TuWebYa.horno.forms.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record FormResponse(
        String id,
        String email,
        String businessName,
        String businessInfo,
        String objectives,
        String preferences,
        UUID userId,
        UUID assignedAdminId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

