package com.TuWebYa.horno.auth.application.usecase;

import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import com.TuWebYa.horno.auth.application.port.in.LoginUseCase;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LoginUseCaseImpl implements LoginUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUseCaseImpl(UserRepositoryPort userRepositoryPort,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<LoginAuthResponse> login(String email, String password) {
        return userRepositoryPort.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .flatMap(user -> {
                    if (!passwordEncoder.matches(password, user.getPassword().hashed())) {
                        return Mono.error(new RuntimeException("Invalid credentials"));
                    }

                    String token = jwtService.generateToken(
                            user.getId().value().toString(),
                            user.getRole().name()
                    );

                    return Mono.just(new LoginAuthResponse(token));
                });
    }

}
