package com.TuWebYa.horno.user.infra.web;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.command.UpdateUserCommand;
import com.TuWebYa.horno.user.application.dto.request.UpdateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.application.dto.request.CreateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import com.TuWebYa.horno.user.application.port.in.RetrieveUserUseCase;
import com.TuWebYa.horno.user.application.port.in.UpdateUserUseCase;
import com.TuWebYa.horno.user.application.query.RetrieveUserQuery;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Tag(name = "Users", description = "Operations related to users management")
public class HttpUserController {
    private final CreateUserUseCase createUserUseCase;
    private final RetrieveUserUseCase retrieveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    public HttpUserController(CreateUserUseCase createUserUseCase,
                              RetrieveUserUseCase retrieveUserUseCase,
                              UpdateUserUseCase updateUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.retrieveUserUseCase = retrieveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<CreateUserResponse>> create(@RequestBody Mono<CreateUserRequest> requestMono) {
        return requestMono
                .map(request -> new CreateUserCommand(
                        request.name(),
                        request.password(),
                        request.email(),
                        request.role()
                ))
                .flatMap(createUserUseCase::createUser)
                .map(response -> ResponseEntity.created(URI.create("/user/" + response.id())).body(
                        new CreateUserResponse(
                                response.id(),
                                response.name(),
                                response.email(),
                                response.role()
                        )
                ));
    }

    @GetMapping
    public Flux<RetrieveUserResponse> retrieveAll() {
        return retrieveUserUseCase.retrieveAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<RetrieveUserResponse> retrieve(@PathVariable UUID id) {
        return retrieveUserUseCase.retrieveById(new RetrieveUserQuery(id));
    }

    @PutMapping("/{id}")
    public Mono<UpdateUserResponse> update(@PathVariable UUID id, @RequestBody Mono<UpdateUserRequest> requestMono) {
        return requestMono
                .map(request -> new UpdateUserCommand(
                        id,
                        request.name(),
                        request.email(),
                        request.role(),
                        request.password()
                ))
                .flatMap(updateUserUseCase::update)
                .map(user -> new UpdateUserResponse(
                        user.id(),
                        user.name(),
                        user.email(),
                        user.role()
                ));
    }
}
