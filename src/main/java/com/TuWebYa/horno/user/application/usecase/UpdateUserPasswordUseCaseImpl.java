package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.UpdateUserPasswordCommand;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
import com.TuWebYa.horno.user.application.port.in.UpdateUserPasswordUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.exception.InvalidPasswordException;
import com.TuWebYa.horno.user.domain.model.UserPassword;
import reactor.core.publisher.Mono;

public class UpdateUserPasswordUseCaseImpl implements UpdateUserPasswordUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public UpdateUserPasswordUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<Void> updatePassword(UpdateUserPasswordCommand command) {
        return userRepositoryPort.findById(command.id())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User with id: " + command.id() + " not found.")))
                .flatMap(user -> {
                            if (!command.authenticatedUserId().equals(command.id())
                                    && command.authenticatedUserRole().equals("USER")) {
                                return Mono.error(new UserForbiddenException());
                            }

                            if (command.authenticatedUserRole().equals("USER")) {
                                if (!user.getPassword().matches(command.oldPassword())) {
                                    return Mono.error(new InvalidPasswordException("Passwords cannot be the same"));
                                }
                            }
                            user.setPassword(UserPassword.fromPlainText(command.newPassword()));
                            return userRepositoryPort.save(user).then();
                        }
                );
    }
}
