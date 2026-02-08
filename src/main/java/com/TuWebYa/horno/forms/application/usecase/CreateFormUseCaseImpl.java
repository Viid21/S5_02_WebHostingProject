package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.forms.application.command.CreateFormCommand;
import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import com.TuWebYa.horno.forms.application.port.in.CreateFormUseCase;
import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.forms.domain.model.*;
import com.TuWebYa.horno.user.domain.model.UserEmail;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CreateFormUseCaseImpl implements CreateFormUseCase {
    
    private final FormRepositoryPort formRepositoryPort;
    
    public CreateFormUseCaseImpl(FormRepositoryPort formRepositoryPort) {
        this.formRepositoryPort = formRepositoryPort;
    }
    
    @Override
    public Mono<FormResponse> createForm(CreateFormCommand command) {
        UserEmail email = UserEmail.of(command.email());
        
        Form form = Form.create(
                email,
                BusinessName.of(command.businessName()),
                BusinessInfo.of(command.businessInfo()),
                Objectives.of(command.objectives()),
                Preferences.of(command.preferences()),
                command.userId() != null ? UserId.from(command.userId().toString()) : null
        );
        
        if (command.assignedAdminId() != null) {
            form.assignAdmin(UserId.from(command.assignedAdminId().toString()));
        }
        
        return formRepositoryPort.save(form)
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

