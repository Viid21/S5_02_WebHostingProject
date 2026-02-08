package com.TuWebYa.horno.forms.application.port.in;

import com.TuWebYa.horno.forms.application.dto.request.CreateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormSubmissionResponse;
import reactor.core.publisher.Mono;

public interface SubmitFormUseCase {
    Mono<FormSubmissionResponse> submitForm(CreateFormRequest request);
}

