package com.TuWebYa.horno.user.application.usecase;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.user.domain.exception.InvalidEmailException;
import com.TuWebYa.horno.user.domain.model.*;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUserUseCaseImpl implements CreateUserUseCase {
    private final UserRepositoryPort userRepositoryPort;

    public CreateUserUseCaseImpl(UserRepositoryPort userRepositoryPort) {
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<CreateUserResponse> createUser(CreateUserCommand command) {

        return userRepositoryPort.findByEmail(command.email())
                .hasElement()
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new InvalidEmailException("A user with that email already exists."));
                    }

                    if (!command.authenticatedUserRole().equals("SUPERADMIN")) {
                        if (command.role().equals("ADMIN") || command.role().equals("SUPERADMIN")) {
                            return Mono.error(new UserForbiddenException());
                        }
                    }

                    User user = new User(
                            UserEmail.of(command.email()),
                            UserPassword.fromPlainText(command.password()),
                            UserRole.valueOf(command.role())
                    );

                    user.setName(UserName.of(user.getEmail().value().split("@")[0]));

                    return userRepositoryPort.save(user)
                            .map(saved -> new CreateUserResponse(
                                    saved.getId().toString(),
                                    saved.getName().value(),
                                    saved.getEmail().value(),
                                    saved.getRole().name()
                            ));
                });
    }
}
