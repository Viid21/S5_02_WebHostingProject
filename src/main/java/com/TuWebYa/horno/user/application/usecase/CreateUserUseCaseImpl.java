package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.domain.model.*;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import reactor.core.publisher.Mono;

public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public CreateUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserCommand command) {
        User user = new User(
                null,
                UserName.of(command.name()),
                UserEmail.of(command.email()),
                UserPassword.fromPlainText(command.password()),
                UserRole.valueOf(command.role())
        );

        return userRepositoryPort.save(user)
                .map(saved -> new CreateUserResponse(
                        saved.getId().toString(),
                        saved.getName().value(),
                        saved.getEmail().value(),
                        saved.getRole().name()
                ));
    }
}
