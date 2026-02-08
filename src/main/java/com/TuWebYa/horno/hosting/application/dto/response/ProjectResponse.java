package com.TuWebYa.horno.hosting.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProjectResponse(
        String id,
        String formId,
        UUID userId,
        String status,
        String description,
        UUID assignedAdminId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

