package com.TuWebYa.horno;

import com.TuWebYa.horno.auth.infra.security.SecurityContextService;
import com.TuWebYa.horno.user.application.dto.request.CreateUserRequest;
import com.TuWebYa.horno.user.application.dto.request.UpdateUserPasswordRequest;
import com.TuWebYa.horno.user.application.dto.request.UpdateUserRequest;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import com.TuWebYa.horno.user.application.port.in.*;
import com.TuWebYa.horno.user.infra.web.HttpUserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HttpUserControllerTest {

    @Mock
    private SecurityContextService securityContextService;

    @Mock
    private CreateUserUseCase createUserUseCase;

    @Mock
    private RetrieveUserUseCase retrieveUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private UpdateUserPasswordUseCase updateUserPasswordUseCase;

    @Mock
    private DeleteUserUseCase deleteUserUseCase;

    @InjectMocks
    private HttpUserController controller;

    private WebTestClient webTestClient;
    private UUID testUserId;
    private UUID testUserId2;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToController(controller).build();
        testUserId = UUID.randomUUID();
        testUserId2 = UUID.randomUUID();
    }

    @Test
    void me_shouldReturnCurrentUser() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(testUserId));

        RetrieveUserResponse response = new RetrieveUserResponse(
                testUserId.toString(),
                "TestUser",
                "test@example.com",
                "USER"
        );

        when(retrieveUserUseCase.retrieveById(any()))
                .thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/user/me")
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveUserResponse.class)
                .value(res -> {
                    assert res.id().equals(testUserId.toString());
                    assert res.name().equals("TestUser");
                    assert res.email().equals("test@example.com");
                    assert res.role().equals("USER");
                });
    }

    @Test
    void retrieveAll_shouldReturnAllUsers() {
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("ADMIN"));

        RetrieveUserResponse user1 = new RetrieveUserResponse(
                testUserId.toString(),
                "User1",
                "user1@example.com",
                "USER"
        );
        RetrieveUserResponse user2 = new RetrieveUserResponse(
                testUserId2.toString(),
                "User2",
                "user2@example.com",
                "ADMIN"
        );

        when(retrieveUserUseCase.retrieveAllUsers(any()))
                .thenReturn(Flux.just(user1, user2));

        webTestClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RetrieveUserResponse.class)
                .hasSize(2);
    }

    @Test
    void retrieve_shouldReturnUserById() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(testUserId));
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("ADMIN"));

        RetrieveUserResponse response = new RetrieveUserResponse(
                testUserId2.toString(),
                "User2",
                "user2@example.com",
                "USER"
        );

        when(retrieveUserUseCase.retrieveById(any()))
                .thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/user/" + testUserId2)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RetrieveUserResponse.class)
                .value(res -> {
                    assert res.id().equals(testUserId2.toString());
                    assert res.name().equals("User2");
                });
    }

    @Test
    void create_shouldCreateUser() {
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("ADMIN"));

        CreateUserResponse response = new CreateUserResponse(
                testUserId.toString(),
                "NewUser",
                "newuser@example.com",
                "USER"
        );

        when(createUserUseCase.createUser(any()))
                .thenReturn(Mono.just(response));

        CreateUserRequest request = new CreateUserRequest(
                null,
                null,
                "Password123!",
                "newuser@example.com",
                "USER"
        );

        webTestClient.post()
                .uri("/user/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(CreateUserResponse.class)
                .value(res -> {
                    assert res.id().equals(testUserId.toString());
                    assert res.email().equals("newuser@example.com");
                });
    }

    @Test
    void update_shouldUpdateUser() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(testUserId));
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("ADMIN"));

        UpdateUserResponse response = new UpdateUserResponse(
                testUserId2.toString(),
                "UpdatedName",
                "updated@example.com",
                "USER"
        );

        when(updateUserUseCase.update(any()))
                .thenReturn(Mono.just(response));

        UpdateUserRequest request = new UpdateUserRequest(
                "UpdatedName",
                "updated@example.com",
                "USER"
        );

        webTestClient.put()
                .uri("/user/" + testUserId2)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UpdateUserResponse.class)
                .value(res -> {
                    assert res.id().equals(testUserId2.toString());
                    assert res.name().equals("UpdatedName");
                    assert res.email().equals("updated@example.com");
                });
    }

    @Test
    void updatePassword_shouldUpdatePassword() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(testUserId));
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("USER"));

        when(updateUserPasswordUseCase.updatePassword(any()))
                .thenReturn(Mono.empty());

        UpdateUserPasswordRequest request = new UpdateUserPasswordRequest(
                "OldPassword123!",
                "NewPassword123!"
        );

        webTestClient.put()
                .uri("/user/" + testUserId + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void delete_shouldDeleteUser() {
        when(securityContextService.currentUserId())
                .thenReturn(Mono.just(testUserId));
        when(securityContextService.currentUserRole())
                .thenReturn(Mono.just("ADMIN"));

        when(deleteUserUseCase.deleteUser(any()))
                .thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/user/" + testUserId2)
                .exchange()
                .expectStatus().isNoContent();
    }
}

