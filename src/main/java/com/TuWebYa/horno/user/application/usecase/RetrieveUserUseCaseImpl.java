package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.port.in.RetrieveUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class RetrieveUserUseCaseImpl implements RetrieveUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public RetrieveUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<User> executeById(UUID id) {
        return null;
    }

    @Override
    public Flux<RetrieveUserResponse> executeAllUsers() {
        return userRepositoryPort.findAll()
                .map(user -> new RetrieveUserResponse(
                        user.getId().toString(),
                        user.getName().value(),
                        user.getEmail().value(),
                        user.getRole().name()
                ));
    }
}
