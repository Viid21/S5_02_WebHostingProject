package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.dto.response.CreateUserResponse;
import reactor.core.publisher.Mono;

public interface CreateUserUseCase {
    Mono<CreateUserResponse> execute(CreateUserCommand command);

}
