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

                    if (command.role().equals("SUPERADMIN")) {
                        return Mono.error(new UserForbiddenException("Cannot create user with SUPERADMIN role"));
                    }

                    if (!command.authenticatedUserRole().equals("SUPERADMIN")) {
                        if (command.role().equals("ADMIN")) {
                            return Mono.error(new UserForbiddenException());
                        }
                    }

                    User user = new User(
                            UserEmail.of(command.email()),
                            UserPassword.fromPlainText(command.password()),
                            UserRole.valueOf(command.role())
                    );

                    String raw = command.email().split("@")[0];
                    String cleaned = raw.replaceAll("[^A-Za-z]", "");
                    String capitalized = cleaned.substring(0,1).toUpperCase() + cleaned.substring(1);

                    user.setName(UserName.of(capitalized));

                    return userRepositoryPort.save(user)
                            .map(saved -> new CreateUserResponse(
                                    saved.getId().value().toString(),
                                    saved.getName().value(),
                                    saved.getEmail().value(),
                                    saved.getRole().name()
                            ));
                });
    }
}
