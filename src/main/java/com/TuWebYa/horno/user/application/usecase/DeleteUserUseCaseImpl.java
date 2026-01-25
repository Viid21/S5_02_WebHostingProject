package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.DeleteUserCommand;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.port.in.DeleteUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteUserUseCaseImpl implements DeleteUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public DeleteUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<Void> deleteUser(DeleteUserCommand command) {
        if (!command.authenticatedUserId().equals(command.id())
                && command.authenticatedUserRole().equals("USER")) {
            return Mono.error(new UserForbiddenException());
        }

        return userRepositoryPort.deleteById(command.id());
    }
}
