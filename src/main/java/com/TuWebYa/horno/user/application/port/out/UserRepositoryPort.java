package com.TuWebYa.horno.user.application.port.out;

import com.TuWebYa.horno.user.domain.model.User;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {
    Mono<User> save(User newUser);
    Optional<User> findById(UUID id);
    List<User> findAll();
    Optional<User> update(UUID id, User user);
    boolean deleteById(UUID id);
}
