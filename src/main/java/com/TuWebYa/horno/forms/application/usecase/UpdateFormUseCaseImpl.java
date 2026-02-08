package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.forms.application.dto.request.UpdateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import com.TuWebYa.horno.forms.application.exception.FormNotFoundException;
import com.TuWebYa.horno.forms.application.port.in.UpdateFormUseCase;
import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.forms.domain.model.*;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UpdateFormUseCaseImpl implements UpdateFormUseCase {
    
    private final FormRepositoryPort formRepositoryPort;
    
    public UpdateFormUseCaseImpl(FormRepositoryPort formRepositoryPort) {
        this.formRepositoryPort = formRepositoryPort;
    }
    
    @Override
    public Mono<FormResponse> updateForm(UUID userId, UpdateFormRequest request) {
        return formRepositoryPort.findByUserId(UserId.from(userId.toString()))
                .switchIfEmpty(Mono.error(new FormNotFoundException("Form not found for user: " + userId)))
                .flatMap(form -> {
                    form.update(
                            BusinessName.of(request.businessName()),
                            BusinessInfo.of(request.businessInfo()),
                            Objectives.of(request.objectives()),
                            Preferences.of(request.preferences())
                    );
                    
                    return formRepositoryPort.save(form)
                            .map(this::toResponse);
                });
    }
    
    private FormResponse toResponse(com.TuWebYa.horno.forms.domain.model.Form form) {
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

