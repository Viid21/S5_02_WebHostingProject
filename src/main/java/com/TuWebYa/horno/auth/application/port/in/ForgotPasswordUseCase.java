package com.TuWebYa.horno.auth.application.port.in;

import com.TuWebYa.horno.auth.application.query.ForgotPasswordQuery;
import reactor.core.publisher.Mono;

public interface ForgotPasswordUseCase {
    Mono<Void> sendEmail (ForgotPasswordQuery query);
}
