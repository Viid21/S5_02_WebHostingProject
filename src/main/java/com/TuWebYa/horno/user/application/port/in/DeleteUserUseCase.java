package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.command.DeleteUserCommand;
import reactor.core.publisher.Mono;

public interface DeleteUserUseCase {
    Mono<Void> deleteUser(DeleteUserCommand command);
}
