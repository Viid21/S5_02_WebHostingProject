package com.TuWebYa.horno.auth.infra.web;

import com.TuWebYa.horno.auth.application.dto.request.LoginAuthRequest;
import com.TuWebYa.horno.auth.application.dto.request.RegisterAuthRequest;
import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RefreshAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RegisterAuthResponse;
import com.TuWebYa.horno.auth.application.port.in.LoginUseCase;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class HttpAuthController {
    private final CreateUserUseCase createUserUseCase;
    private final JwtService jwtService;
    private final LoginUseCase loginUseCase;
    private final SecurityContextService securityContextService;

    public HttpAuthController(CreateUserUseCase createUserUseCase,
                              JwtService jwtService,
                              LoginUseCase loginUseCase,
                              SecurityContextService securityContextService) {
        this.createUserUseCase = createUserUseCase;
        this.jwtService = jwtService;
        this.loginUseCase = loginUseCase;
        this.securityContextService = securityContextService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterAuthResponse>> register(@RequestBody RegisterAuthRequest request) {
        System.out.println("REGISTER REQUEST: " + request);
        CreateUserCommand command = new CreateUserCommand(
                request.email(),
                request.password(),
                "USER",
                "SUPERADMIN"
        );

        return createUserUseCase.createUser(command)
                .map(user -> {
                    String accessToken = jwtService.generateAccessToken(user.id(), user.role());
                    String refreshToken = jwtService.generateRefreshToken(user.id());

                    return ResponseEntity.ok(new RegisterAuthResponse(accessToken, refreshToken));
                });
    }

    @PostMapping("/login")
    public Mono<LoginAuthResponse> login(@RequestBody LoginAuthRequest request) {
        return loginUseCase.login(request.email(), request.password());
    }

    @PostMapping("/refresh")
    public Mono<RefreshAuthResponse> refresh(){
        return Mono.zip(
                securityContextService.currentUserId(),
                securityContextService.currentUserRole()
        ).map(tuple -> {
            UUID userId = tuple.getT1();
            String role = tuple.getT2();

            String newToken = jwtService.generateAccessToken(userId.toString(), role);

            return new RefreshAuthResponse(newToken);
        });

    }

    @GetMapping("/validate")
    public Mono<ResponseEntity<Void>> validate() {
        return securityContextService.currentUserId()
                .map(id -> ResponseEntity.ok().<Void>build());
    }
}
