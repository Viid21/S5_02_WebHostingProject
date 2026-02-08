package com.TuWebYa.horno.forms.application.port.in;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CheckFormExistsUseCase {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUserId(UUID userId);
}

