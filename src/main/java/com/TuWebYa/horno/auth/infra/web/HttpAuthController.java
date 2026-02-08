package com.TuWebYa.horno.auth.infra.web;

import com.TuWebYa.horno.auth.application.dto.request.ForgotPasswordRequest;
import com.TuWebYa.horno.auth.application.dto.request.LoginAuthRequest;
import com.TuWebYa.horno.auth.application.dto.request.RegisterAuthRequest;
import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RefreshAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RegisterAuthResponse;
import com.TuWebYa.horno.auth.application.port.in.ForgotPasswordUseCase;
import com.TuWebYa.horno.auth.application.port.in.LoginUseCase;
import com.TuWebYa.horno.auth.application.query.ForgotPasswordQuery;
import com.TuWebYa.horno.auth.application.query.LoginQuery;
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
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    public HttpAuthController(CreateUserUseCase createUserUseCase,
                              JwtService jwtService,
                              LoginUseCase loginUseCase,
                              SecurityContextService securityContextService, ForgotPasswordUseCase forgotPasswordUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.jwtService = jwtService;
        this.loginUseCase = loginUseCase;
        this.securityContextService = securityContextService;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterAuthResponse>> register(@RequestBody RegisterAuthRequest request) {
        CreateUserCommand command = new CreateUserCommand(
                request.email(),
                request.password(),
                "USER",
                "PUBLIC"
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
        return loginUseCase.login(new LoginQuery(request.email(), request.password()));
    }

    @PostMapping("/forgot-password")
    public Mono<ResponseEntity<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return forgotPasswordUseCase.sendEmail(new ForgotPasswordQuery(request.mail()))
                .then(Mono.just(ResponseEntity.ok().<Void>build()));
    }

    @PostMapping("/refresh")
    public Mono<RefreshAuthResponse> refresh() {
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
