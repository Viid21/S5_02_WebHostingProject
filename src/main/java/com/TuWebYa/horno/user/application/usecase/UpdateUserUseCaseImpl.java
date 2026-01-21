package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.UpdateUserCommand;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.application.port.in.UpdateUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.*;
import reactor.core.publisher.Mono;

public class UpdateUserUseCaseImpl implements UpdateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<UpdateUserResponse> update(UpdateUserCommand command) {
        return userRepositoryPort.findById(command.id())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id: " + command.id() + " not found.")))
                .flatMap(existing -> {
                    User updated = new User(
                            UserId.from(command.id().toString()),
                            command.name() != null ? UserName.of(command.name()) : existing.getName(),
                            command.email() != null ? UserEmail.of(command.email()) : existing.getEmail(),
                            command.password() != null ? UserPassword.fromPlainText(command.password()) :
                            UserPassword.fromHashed(existing.getPassword().hashed()),
                            command.role() != null ? UserRole.valueOf(command.role()) : existing.getRole()
                    );
                    return userRepositoryPort.save(updated);
                })
                .map(saved -> new UpdateUserResponse(
                        saved.getId().value().toString(),
                        saved.getName().value(),
                        saved.getEmail().value(),
                        saved.getRole().name()
                ));
    }
}
