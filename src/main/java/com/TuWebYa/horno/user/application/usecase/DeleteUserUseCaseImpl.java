package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.DeleteUserCommand;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.application.exception.UserNotFoundException;
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
        return userRepositoryPort.findById(command.id())
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")))
                .flatMap(userToDelete -> {
                    if (!command.authenticatedUserId().equals(command.id())
                            && command.authenticatedUserRole().equals("USER")) {
                        return Mono.error(new UserForbiddenException());
                    }

                    if (command.authenticatedUserRole().equals("ADMIN")
                            && (userToDelete.getRole() == com.TuWebYa.horno.user.domain.model.UserRole.ADMIN
                            || userToDelete.getRole() == com.TuWebYa.horno.user.domain.model.UserRole.SUPERADMIN)) {
                        return Mono.error(new UserForbiddenException("Admin cannot delete another admin or superadmin"));
                    }
                    
                    return userRepositoryPort.deleteById(command.id());
                });
    }
}
