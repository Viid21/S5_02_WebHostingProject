package com.TuWebYa.horno.user.application.port.in;

import com.TuWebYa.horno.user.application.dto.response.RetrieveUserResponse;
import com.TuWebYa.horno.user.application.query.RetrieveUserAllQuery;
import com.TuWebYa.horno.user.application.query.RetrieveUserQuery;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RetrieveUserUseCase {
    Mono<RetrieveUserResponse> retrieveById(RetrieveUserQuery query);
    Flux<RetrieveUserResponse> retrieveAllUsers(RetrieveUserAllQuery query);
}
