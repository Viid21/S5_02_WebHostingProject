package com.TuWebYa.horno.auth.application.usecase;

import com.TuWebYa.horno.auth.application.port.in.ForgotPasswordUseCase;
import com.TuWebYa.horno.auth.application.query.ForgotPasswordQuery;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ForgotPasswordUseCaseImpl implements ForgotPasswordUseCase {
    private final UserRepositoryPort userRepositoryPort;
    private final JwtService jwtService;

    public ForgotPasswordUseCaseImpl(UserRepositoryPort userRepositoryPort, JwtService jwtService) {
        this.userRepositoryPort = userRepositoryPort;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> sendEmail(ForgotPasswordQuery query) {
        return userRepositoryPort.findByEmail(query.email())
                .flatMap(user -> {
                    String token = jwtService.generateResetPasswordToken(user.getId().toString());

                    String resetLink = "https://tuwebya.com/reset-password?token=" + token;

                    // TODO: enviar email real
                    System.out.println("RESET LINK PARA " + user.getEmail() + ": " + resetLink);

                    // cuando tengas EmailService:
                    // return emailService.sendResetPasswordEmail(user.email(), resetLink).then(Mono.just(user));

                    return Mono.empty();
                });
    }
}
