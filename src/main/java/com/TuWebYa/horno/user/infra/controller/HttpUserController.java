package com.TuWebYa.horno.user.infra.controller;

import com.TuWebYa.horno.user.domain.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.infra.dto.request.CreateUserRequestBodyDto;
import com.TuWebYa.horno.user.infra.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/User")
@Tag(name = "Users", description = "Operations related to users management")
public class HttpUserController {
    private final CreateUserUseCase createUserUseCase;

    public HttpUserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/new")
    public Mono<ResponseEntity<UserResponseDto>> create(@RequestBody CreateUserRequestBodyDto request){

    }

}
