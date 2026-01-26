package com.TuWebYa.horno.auth.infra.web;

import com.TuWebYa.horno.auth.application.dto.request.RegisterAuthRequest;
import com.TuWebYa.horno.auth.application.dto.response.RegisterAuthResponse;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class HttpAuthController {
    private final CreateUserUseCase createUserUseCase;
    private final JwtService jwtService;

    public HttpAuthController(CreateUserUseCase createUserUseCase,
                              JwtService jwtService) {
        this.createUserUseCase = createUserUseCase;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterAuthResponse>> register(Mono<RegisterAuthRequest> monoRequest) {
        return monoRequest
                .flatMap(request -> {
                    CreateUserCommand command = new CreateUserCommand(
                            request.email(),
                            request.password(),
                            "USER",
                            "SUPERADMIN"
                    );

                    return createUserUseCase.createUser(command)
                            .flatMap(user -> {
                                String token = jwtService.generateToken(
                                        user.id(),
                                        user.role()
                                );

                                RegisterAuthResponse response = new RegisterAuthResponse(
                                        user.id(),
                                        user.email(),
                                        user.role(),
                                        token
                                );
                                return Mono.just(ResponseEntity.ok(response));
                            });
                });
    }
}
