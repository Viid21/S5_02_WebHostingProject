package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.command.UpdateUserCommand;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import reactor.core.publisher.Mono;

public interface UpdateUserUseCase {
    Mono<UpdateUserResponse> update(UpdateUserCommand command);
}
