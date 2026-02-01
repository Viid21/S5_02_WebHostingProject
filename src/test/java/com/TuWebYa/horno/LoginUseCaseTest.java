package com.TuWebYa.horno;

import com.TuWebYa.horno.auth.application.exception.InvalidCredentialsException;
import com.TuWebYa.horno.auth.application.usecase.LoginUseCaseImpl;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        User user = new User(UserEmail.of("email@test.com"), UserPassword.fromPlainText("Estegosaurio9000@"), UserRole.USER);
        user.setId(UserId.from("539490b3-471e-44f4-b256-818262d2a0a3"));

        when(userRepository.findByEmail("email@test.com"))
                .thenReturn(Mono.just(user));

        when(passwordEncoder.matches(eq("1234"), anyString()))
                .thenReturn(true);

        when(jwtService.generateAccessToken("539490b3-471e-44f4-b256-818262d2a0a3", "USER"))
                .thenReturn("ACCESS");

        when(jwtService.generateRefreshToken("539490b3-471e-44f4-b256-818262d2a0a3"))
                .thenReturn("REFRESH");

        loginUseCase.login("email@test.com", "1234")
                .as(StepVerifier::create)
                .expectNextMatches(res ->
                        res.accessToken().equals("ACCESS") &&
                                res.refreshToken().equals("REFRESH")
                )
                .verifyComplete();
    }

    @Test
    void login_shouldError_whenUserNotFound() {
        when(userRepository.findByEmail("missing@test.com"))
                .thenReturn(Mono.empty());

        loginUseCase.login("missing@test.com", "1234")
                .as(StepVerifier::create)
                .expectErrorMatches(err ->
                        err instanceof InvalidCredentialsException &&
                                err.getMessage().equals("Invalid credentials.")
                )
                .verify();
    }

    @Test
    void login_shouldError_whenPasswordDoesNotMatch() {
        String hashedPassword = "$2a$10$pxt8dNLX8qy0dwRlSMWfueQXGtgE2Cbj28Qi0wcUDiAzoZbVQMTVe";

        User user = new User(UserEmail.of("email@test.com"), UserPassword.fromPlainText("Estegosaurio9000@"), UserRole.USER);

        when(userRepository.findByEmail("email@test.com"))
                .thenReturn(Mono.just(user));

        when(passwordEncoder.matches(eq("wrong"), anyString()))
                .thenReturn(false);

        loginUseCase.login("email@test.com", "wrong")
                .as(StepVerifier::create)
                .expectErrorMatches(err ->
                        err instanceof InvalidCredentialsException &&
                                err.getMessage().equals("Invalid credentials.")
                )
                .verify();
    }
}