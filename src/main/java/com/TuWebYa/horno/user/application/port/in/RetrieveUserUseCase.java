package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RetrieveUserUseCase {
    Mono<User> executeById(UUID id);
    Flux<RetrieveUserResponse> executeAllUsers();
}
