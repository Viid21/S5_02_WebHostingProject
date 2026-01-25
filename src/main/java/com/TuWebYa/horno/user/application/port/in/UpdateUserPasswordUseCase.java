package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.command.UpdateUserPasswordCommand;
import reactor.core.publisher.Mono;

public interface UpdateUserPasswordUseCase {
    Mono<Void> updatePassword(UpdateUserPasswordCommand command);
}
