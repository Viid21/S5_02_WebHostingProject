package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.DeleteUserCommand;
import com.TuWebYa.horno.user.application.port.in.DeleteUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import reactor.core.publisher.Mono;

public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public DeleteUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<Void> deleteUser(DeleteUserCommand command) {
        return userRepositoryPort.deleteById(command.id());
    }
}
