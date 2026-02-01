package com.TuWebYa.horno;

import com.TuWebYa.horno.auth.application.dto.request.LoginAuthRequest;
import com.TuWebYa.horno.auth.application.dto.request.RegisterAuthRequest;
import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RefreshAuthResponse;
import com.TuWebYa.horno.auth.application.dto.response.RegisterAuthResponse;
import com.TuWebYa.horno.auth.application.port.in.LoginUseCase;
import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.auth.infra.web.HttpAuthController;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpAuthControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private JwtService jwtService;

    @Mock
    private LoginUseCase loginUseCase;

    @Mock
    private SecurityContextService securityContextService;

    @InjectMocks
    private HttpAuthController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void register_shouldReturnTokens() {

        when(createUserUseCase.createUser(any(CreateUserCommand.class)))
                .thenReturn(Mono.just(
                        new CreateUserResponse(
                                "id123",
                                "Test",
                                "email@test.com",
                                "USER"
                        )
                ));

        when(jwtService.generateAccessToken(anyString(), anyString()))
                .thenReturn("ACCESS_TOKEN");

        when(jwtService.generateRefreshToken(anyString()))
                .thenReturn("REFRESH_TOKEN");

        RegisterAuthRequest request = new RegisterAuthRequest("email@test.com", "1234");

        webTestClient.post()
                .uri("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RegisterAuthResponse.class)
                .value(res -> {
                    assert res.accessToken().equals("ACCESS_TOKEN");
                    assert res.refreshToken().equals("REFRESH_TOKEN");
                });
    }

    @Test
    void login_shouldReturnLoginResponse() {
        LoginAuthResponse response = new LoginAuthResponse("ACCESS", "REFRESH");

        when(loginUseCase.login("email@test.com", "1234"))
                .thenReturn(Mono.just(response));

        LoginAuthRequest request = new LoginAuthRequest("email@test.com", "1234");

        webTestClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginAuthResponse.class)
                .value(res -> {
                    assert res.accessToken().equals("ACCESS");
                    assert res.refreshToken().equals("REFRESH");
                });
    }

    @Test
    void refresh_shouldReturnNewToken() {

        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(UUID.randomUUID()));

        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("USER"));

        when(jwtService.generateAccessToken(anyString(), anyString()))
                .thenReturn("NEW_ACCESS");

        webTestClient.post()
                .uri("/auth/refresh")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RefreshAuthResponse.class)
                .value((RefreshAuthResponse res) -> {
                    assert res.accessToken().equals("NEW_ACCESS");
                });
    }

    @Test
    void validate_shouldReturnOk() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(UUID.randomUUID()));

        webTestClient.get()
                .uri("/auth/validate")
                .exchange()
                .expectStatus().isOk();
    }
}