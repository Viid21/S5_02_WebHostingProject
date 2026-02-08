package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.forms.application.dto.response.FormResponse;
import com.TuWebYa.horno.forms.application.exception.FormNotFoundException;
import com.TuWebYa.horno.forms.application.port.in.AssignAdminToFormUseCase;
import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.user.application.exception.UserForbiddenException;
import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class AssignAdminToFormUseCaseImpl implements AssignAdminToFormUseCase {
    
    private final FormRepositoryPort formRepositoryPort;
    
    public AssignAdminToFormUseCaseImpl(FormRepositoryPort formRepositoryPort) {
        this.formRepositoryPort = formRepositoryPort;
    }
    
    @Override
    public Mono<FormResponse> assignAdmin(String formId, UUID adminId, String authenticatedUserRole) {
        // Solo SUPERADMIN puede asignar admin a formulario
        if (!authenticatedUserRole.equals("SUPERADMIN")) {
            return Mono.error(new UserForbiddenException("Only SUPERADMIN can assign admin to forms"));
        }
        
        return formRepositoryPort.findById(FormId.from(formId))
                .switchIfEmpty(Mono.error(new FormNotFoundException("Form not found: " + formId)))
                .flatMap(form -> {
                    form.assignAdmin(UserId.from(adminId.toString()));
                    return formRepositoryPort.save(form);
                })
                .map(form -> new FormResponse(
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
                ));
    }
}

