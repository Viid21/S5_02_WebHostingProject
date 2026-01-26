package com.TuWebYa.horno.auth.application.port.in;

import com.TuWebYa.horno.auth.application.dto.response.LoginAuthResponse;
import reactor.core.publisher.Mono;

public interface LoginUseCase {
    Mono<LoginAuthResponse> login(String email, String password);
}
