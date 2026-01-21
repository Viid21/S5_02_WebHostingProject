package com.TuWebYa.horno.user.application.port.out;

import com.TuWebYa.horno.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserRepositoryPort {
    Mono<User> save(User newUser);
    Mono<User> findById(UUID id);
    Flux<User> findAll();
    Mono<Void> deleteById(UUID id);
}
