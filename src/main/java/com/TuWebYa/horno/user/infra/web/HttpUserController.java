package com.TuWebYa.horno.user.infra.web;

import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.command.DeleteUserCommand;
import com.TuWebYa.horno.user.application.command.UpdateUserCommand;
import com.TuWebYa.horno.user.application.command.UpdateUserPasswordCommand;
import com.TuWebYa.horno.user.application.dto.request.UpdateUserPasswordRequest;
import com.TuWebYa.horno.user.application.dto.request.UpdateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import com.TuWebYa.horno.user.application.port.in.*;
import com.TuWebYa.horno.user.application.dto.request.CreateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import com.TuWebYa.horno.user.application.query.RetrieveUserAllQuery;
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
    private final SecurityContextService securityContextService;
    private final CreateUserUseCase createUserUseCase;
    private final RetrieveUserUseCase retrieveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdateUserPasswordUseCase updateUserPasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;


    public HttpUserController(
            SecurityContextService securityContextService, CreateUserUseCase createUserUseCase,
            RetrieveUserUseCase retrieveUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            UpdateUserPasswordUseCase updateUserPasswordUseCase,
            DeleteUserUseCase deleteUserUseCase) {
        this.securityContextService = securityContextService;
        this.createUserUseCase = createUserUseCase;
        this.retrieveUserUseCase = retrieveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.updateUserPasswordUseCase = updateUserPasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<CreateUserResponse>> create(@RequestBody Mono<CreateUserRequest> requestMono) {

        return Mono.zip(
                requestMono,
                securityContextService.currentUserRole()
        ).flatMap(tuple -> {

            CreateUserRequest request = tuple.getT1();
            String userRole = tuple.getT2();

            CreateUserCommand command = new CreateUserCommand(
                    request.email(),
                    request.password(),
                    request.role(),
                    userRole
            );

            return createUserUseCase.createUser(command)
                    .map(response ->
                            ResponseEntity
                                    .created(URI.create("/user/" + response.id()))
                                    .body(response)
                    );
        });
    }

    @GetMapping("/me")
    public Mono<RetrieveUserResponse> me() {
        return securityContextService.currentUserId()
                .flatMap(userId -> retrieveUserUseCase.retrieveById(
                        new RetrieveUserQuery(userId, "SELF")
                ));
    }

    @GetMapping
    public Flux<RetrieveUserResponse> retrieveAll() {
        return securityContextService.currentUserRole()
                .flatMapMany(userRole -> retrieveUserUseCase.retrieveAllUsers(
                        new RetrieveUserAllQuery(userRole)
                ));
    }

    @GetMapping("/{id}")
    public Mono<RetrieveUserResponse> retrieve(@PathVariable UUID id) {
        return securityContextService.currentUserRole()
                .flatMap(userRole -> retrieveUserUseCase.retrieveById(
                        new RetrieveUserQuery(
                                id,
                                userRole
                        )
                ));
    }

    @PutMapping("/{id}")
    public Mono<UpdateUserResponse> update(
            @PathVariable UUID id,
            @RequestBody Mono<UpdateUserRequest> requestMono) {
        return Mono.zip(
                        requestMono,
                        securityContextService.currentUserId(),
                        securityContextService.currentUserRole())
                .flatMap(tuple -> {
                    UpdateUserRequest request = tuple.getT1();
                    UUID authenticatedUserId = tuple.getT2();
                    String userRole = tuple.getT3();

                    UpdateUserCommand command = new UpdateUserCommand(
                            id,
                            request.name(),
                            request.email(),
                            request.role(),
                            authenticatedUserId,
                            userRole
                    );
                    return updateUserUseCase.update(command);
                });
    }

    @PutMapping("/{id}/password")
    public Mono<ResponseEntity<Void>> updatePassword(
            @PathVariable UUID id,
            @RequestBody Mono<UpdateUserPasswordRequest> requestMono) {
        return Mono.zip(
                        requestMono,
                        securityContextService.currentUserId(),
                        securityContextService.currentUserRole())
                .flatMap(tuple -> {
                    UpdateUserPasswordRequest request = tuple.getT1();
                    UUID authenticatedUserId = tuple.getT2();
                    String userRole = tuple.getT3();

                    UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(
                            id,
                            request.oldPassword(),
                            request.newPassword(),
                            authenticatedUserId,
                            userRole
                    );

                    return updateUserPasswordUseCase.updatePassword(command).thenReturn(ResponseEntity.noContent()
                            .build());
                });
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable UUID id) {
        return Mono.zip(
                        securityContextService.currentUserId(),
                        securityContextService.currentUserRole())
                .flatMap(tuple -> {
                    UUID authenticatedUserId = tuple.getT1();
                    String userRole = tuple.getT2();

                    DeleteUserCommand command = new DeleteUserCommand(
                            id,
                            authenticatedUserId,
                            userRole
                    );

                    return deleteUserUseCase.deleteUser(command).thenReturn(ResponseEntity.noContent().build());
                });
    }
}
