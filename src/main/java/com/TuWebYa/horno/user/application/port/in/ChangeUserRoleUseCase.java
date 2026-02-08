package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.command.ChangeUserRoleCommand;
import com.TuWebYa.horno.user.application.dto.response.UpdateUserResponse;
import reactor.core.publisher.Mono;

public interface ChangeUserRoleUseCase {
    Mono<UpdateUserResponse> changeRole(ChangeUserRoleCommand command);
}

