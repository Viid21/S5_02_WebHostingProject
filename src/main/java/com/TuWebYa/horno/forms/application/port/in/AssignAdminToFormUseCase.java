package com.TuWebYa.horno.forms.application.port.in;

import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AssignAdminToFormUseCase {
    Mono<FormResponse> assignAdmin(String formId, UUID adminId, String authenticatedUserRole);
}

