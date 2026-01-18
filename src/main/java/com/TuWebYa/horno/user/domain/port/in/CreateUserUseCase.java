package com.TuWebYa.horno.user.domain.port.in;

import com.TuWebYa.horno.user.app.command.CreateUserCommand;
import com.TuWebYa.horno.user.infra.dto.response.UserResponse;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {
    Mono<UserResponse> execute(CreateUserCommand command);

}
