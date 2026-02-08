package com.TuWebYa.horno.forms.application.port.in;

import com.TuWebYa.horno.forms.application.dto.request.UpdateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UpdateFormUseCase {
    Mono<FormResponse> updateForm(UUID userId, UpdateFormRequest request);
}

