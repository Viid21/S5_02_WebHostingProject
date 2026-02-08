package com.TuWebYa.horno.forms.application.port.out;

import com.TuWebYa.horno.forms.domain.model.Form;
import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.user.domain.model.UserEmail;
import com.TuWebYa.horno.user.domain.model.UserId;
import reactor.core.publisher.Mono;

public interface FormRepositoryPort {
    Mono<Form> save(Form form);
    Mono<Form> findById(FormId formId);
    Mono<Form> findByEmail(UserEmail email);
    Mono<Form> findByUserId(UserId userId);
    Mono<Boolean> existsByEmail(UserEmail email);
}

