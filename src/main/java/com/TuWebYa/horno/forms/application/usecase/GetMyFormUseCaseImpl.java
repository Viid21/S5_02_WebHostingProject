package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import com.TuWebYa.horno.forms.application.exception.FormNotFoundException;
import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.forms.domain.model.Form;
import com.TuWebYa.horno.forms.application.port.in.GetMyFormUseCase;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class GetMyFormUseCaseImpl implements GetMyFormUseCase {

    private final FormRepositoryPort formRepositoryPort;

    public GetMyFormUseCaseImpl(FormRepositoryPort formRepositoryPort) {
        this.formRepositoryPort = formRepositoryPort;
    }

    @Override
    public Mono<FormResponse> getMyForm(UUID userId) {
        return formRepositoryPort.findByUserId(UserId.from(userId.toString()))
                .switchIfEmpty(Mono.error(new FormNotFoundException("Form not found for user: " + userId)))
                .map(this::toResponse);
    }

    private FormResponse toResponse(Form form) {
        return new FormResponse(
                form.getId().toString(),
                form.getEmail().value(),
                form.getBusinessName().value(),
                form.getBusinessInfo().value(),
                form.getObjectives().value(),
                form.getPreferences().value(),
                form.getUserId() != null ? form.getUserId().value() : null,
                form.getAssignedAdminId() != null ? form.getAssignedAdminId().value() : null,
                form.getCreatedAt(),
                form.getUpdatedAt()
        );
    }
}

