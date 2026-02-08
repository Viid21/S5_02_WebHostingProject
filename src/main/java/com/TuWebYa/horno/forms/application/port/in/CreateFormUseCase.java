package com.TuWebYa.horno.forms.application.port.in;

import com.TuWebYa.horno.forms.application.command.CreateFormCommand;
import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import reactor.core.publisher.Mono;

public interface CreateFormUseCase {
    Mono<FormResponse> createForm(CreateFormCommand command);
}

