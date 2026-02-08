package com.TuWebYa.horno.forms.application.dto.response;

public record FormSubmissionResponse(
        String message,
        boolean userCreated,
        String temporaryPassword,
        boolean formExists,
        boolean projectCreated,
        String accessToken,
        String refreshToken,
        String userId,
        String formId,
        String projectId
) {}

