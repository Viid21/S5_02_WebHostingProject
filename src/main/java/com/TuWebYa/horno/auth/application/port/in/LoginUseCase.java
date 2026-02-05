package com.TuWebYa.horno.auth.application.port.in;

import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import com.TuWebYa.horno.auth.application.query.LoginQuery;
import reactor.core.publisher.Mono;

public interface LoginUseCase {
    Mono<LoginAuthResponse> login(LoginQuery query);
}
