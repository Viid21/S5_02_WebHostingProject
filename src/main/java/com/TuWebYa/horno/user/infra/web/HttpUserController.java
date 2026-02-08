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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class HttpUserController {
    private final SecurityContextService securityContextService;
    private final CreateUserUseCase createUserUseCase;
    private final RetrieveUserUseCase retrieveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdateUserPasswordUseCase updateUserPasswordUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final ChangeUserRoleUseCase changeUserRoleUseCase;


    public HttpUserController(
            SecurityContextService securityContextService, CreateUserUseCase createUserUseCase,
            RetrieveUserUseCase retrieveUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            UpdateUserPasswordUseCase updateUserPasswordUseCase,
            DeleteUserUseCase deleteUserUseCase,
            ChangeUserRoleUseCase changeUserRoleUseCase) {
        this.securityContextService = securityContextService;
        this.createUserUseCase = createUserUseCase;
        this.retrieveUserUseCase = retrieveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.updateUserPasswordUseCase = updateUserPasswordUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.changeUserRoleUseCase = changeUserRoleUseCase;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<CreateUserResponse>> create(@RequestBody CreateUserRequest request) {
        return securityContextService.currentUserRole()
                .flatMap(userRole -> {
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
                        new RetrieveUserQuery(userId, userId, "SELF")
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
        return Mono.zip(
                securityContextService.currentUserId(),
                securityContextService.currentUserRole()
        ).flatMap(tuple -> {
            UUID authenticatedUserId = tuple.getT1();
            String userRole = tuple.getT2();
            return retrieveUserUseCase.retrieveById(
                    new RetrieveUserQuery(id, authenticatedUserId, userRole)
            );
        });
    }

    @PutMapping("/{id}")
    public Mono<UpdateUserResponse> update(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request) {
        return Mono.zip(
                        securityContextService.currentUserId(),
                        securityContextService.currentUserRole())
                .flatMap(tuple -> {
                    UUID authenticatedUserId = tuple.getT1();
                    String userRole = tuple.getT2();

                    UpdateUserCommand command = new UpdateUserCommand(
                            id,
                            request.name(),
                            request.email(),
                            null, // El rol no se puede cambiar desde este endpoint
                            authenticatedUserId,
                            userRole
                    );
                    return updateUserUseCase.update(command);
                });
    }

    @PutMapping("/{id}/password")
    public Mono<ResponseEntity<Void>> updatePassword(
            @PathVariable UUID id,
            @RequestBody UpdateUserPasswordRequest request) {
        return Mono.zip(
                        securityContextService.currentUserId(),
                        securityContextService.currentUserRole())
                .flatMap(tuple -> {
                    UUID authenticatedUserId = tuple.getT1();
                    String userRole = tuple.getT2();

                    UpdateUserPasswordCommand command = new UpdateUserPasswordCommand(
                            id,
                            request.oldPassword(),
                            request.newPassword(),
                            authenticatedUserId,
                            userRole
                    );

                    return updateUserPasswordUseCase.updatePassword(command)
                            .thenReturn(ResponseEntity.noContent().build());
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
    
    @PutMapping("/{id}/role")
    public Mono<UpdateUserResponse> changeRole(
            @PathVariable UUID id,
            @RequestBody com.TuWebYa.horno.user.application.dto.request.ChangeUserRoleRequest request) {
        return Mono.zip(
                securityContextService.currentUserId(),
                securityContextService.currentUserRole()
        ).flatMap(tuple -> {
            UUID authenticatedUserId = tuple.getT1();
            String userRole = tuple.getT2();
            
            com.TuWebYa.horno.user.application.command.ChangeUserRoleCommand command =
                    new com.TuWebYa.horno.user.application.command.ChangeUserRoleCommand(
                            id,
                            request.newRole(),
                            authenticatedUserId,
                            userRole
                    );
            
            return changeUserRoleUseCase.changeRole(command);
        });
    }
}
