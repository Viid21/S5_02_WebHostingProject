package com.TuWebYa.horno.user.infra.adapter;

import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.User;
import com.TuWebYa.horno.user.infra.mapper.UserMapper;
import com.TuWebYa.horno.user.infra.repository.UserR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UserRepositoryImpl implements UserRepositoryPort {
    private final UserR2dbcRepository repository;

    public UserRepositoryImpl(UserR2dbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<User> save(User newUser) {
        return repository.save(UserMapper.toEntity(newUser))
                .map(UserMapper::toDomain);
    }

    @Override
    public Mono<User> findById(UUID id) {
        return repository.findById(id)
                .map(UserMapper::toDomain);
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(UserMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return repository.deleteById(id);
    }
}
