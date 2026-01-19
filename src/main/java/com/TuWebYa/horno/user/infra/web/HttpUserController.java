package com.TuWebYa.horno.user.infra.web;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.application.dto.request.CreateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import com.TuWebYa.horno.user.application.port.in.RetrieveUserUseCase;
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

    public HttpUserController(CreateUserUseCase createUserUseCase, RetrieveUserUseCase retrieveUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.retrieveUserUseCase = retrieveUserUseCase;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<CreateUserResponse>> create(@RequestBody Mono<CreateUserRequest> requestMono) {
        return requestMono
                .map(request -> new CreateUserCommand(
                        request.name(),
                        request.email(),
                        request.password(),
                        request.role()
                ))
                .flatMap(createUserUseCase::execute)
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
    public Flux<RetrieveUserResponse> retrieveAllUsers() {
        return retrieveUserUseCase.executeAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<RetrieveUserResponse> retrieveUser(@RequestParam UUID id){

    }
}
