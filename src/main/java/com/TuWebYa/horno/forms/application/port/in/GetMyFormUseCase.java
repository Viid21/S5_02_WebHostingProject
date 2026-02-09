package com.TuWebYa.horno.forms.application.port.in;

import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetMyFormUseCase {
    Mono<FormResponse> getMyForm(UUID userId);
}
