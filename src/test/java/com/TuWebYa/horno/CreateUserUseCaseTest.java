package com.TuWebYa.horno;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.application.usecase.CreateUserUseCaseImpl;
import com.TuWebYa.horno.user.domain.model.User;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepositoryPort repository;

    @InjectMocks
    private CreateUserUseCaseImpl useCase;

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserCommand command = new CreateUserCommand(
                "test@gmail.com",
                "Password123!",
                "USER",
                "SUPERADMIN"
        );

        Mockito.when(repository.findByEmail("test@gmail.com"))
                .thenReturn(Mono.empty());

        Mockito.when(repository.save(Mockito.any()))
                .thenAnswer(invocation -> {
                    User u = invocation.getArgument(0);
                    u.setId(UserId.from(UUID.randomUUID().toString()));
                    return Mono.just(u);
                });

        StepVerifier.create(useCase.createUser(command))
                .expectNextMatches(response ->
                        response.email().equals("test@gmail.com")
                )
                .verifyComplete();
    }
}
