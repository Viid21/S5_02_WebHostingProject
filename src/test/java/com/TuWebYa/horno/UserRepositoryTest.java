package com.TuWebYa.horno;

import com.TuWebYa.horno.user.infra.entity.UserEntity;
import com.TuWebYa.horno.user.infra.repository.UserR2dbcRepository;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.junit.jupiter.Container;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.testcontainers.containers.PostgreSQLContainer;
import reactor.test.StepVerifier;

@DataR2dbcTest
@Testcontainers
class UserRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15")
                    .withInitScript("schema.sql");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        //temporal:____________________________________________
        postgres.withLogConsumer(outputFrame -> System.out.print(outputFrame.getUtf8String()));

        registry.add("spring.r2dbc.url", () ->
                "r2dbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/test"
        );
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);
        registry.add("spring.r2dbc.pool.enabled", () -> false);
    }

    @Autowired
    private UserR2dbcRepository repository;

    @Test
    void shouldSaveUser() {
        UserEntity entity = new UserEntity();
        entity.setName("David");
        entity.setEmail("test@gmail.com");
        entity.setPassword("hashed");
        entity.setRole("USER");

        StepVerifier.create(repository.save(entity))
                .expectNextMatches(saved -> saved.getId() != null)
                .verifyComplete();
    }
}