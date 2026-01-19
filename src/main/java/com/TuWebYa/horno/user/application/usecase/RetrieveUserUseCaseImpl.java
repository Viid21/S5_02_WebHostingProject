package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.application.port.in.RetrieveUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.application.query.RetrieveUserQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RetrieveUserUseCaseImpl implements RetrieveUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public RetrieveUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<RetrieveUserResponse> executeById(RetrieveUserQuery query) {
        return userRepositoryPort.findById(query.id())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id: " + query.id() + " not found.")))
                .map(user -> new RetrieveUserResponse(
                        user.getId().toString(),
                        user.getName().value(),
                        user.getEmail().value(),
                        user.getRole().name()
                ));
    }

    @Override
    public Flux<RetrieveUserResponse> executeAllUsers() {
        return userRepositoryPort.findAll()
                .switchIfEmpty(Mono.error(new UserNotFoundException("User list empty.")))
                .map(user -> new RetrieveUserResponse(
                        user.getId().toString(),
                        user.getName().value(),
                        user.getEmail().value(),
                        user.getRole().name()
                ));
    }
}
