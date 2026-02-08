package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.common.service.PasswordGeneratorService;
import com.TuWebYa.horno.forms.application.dto.request.CreateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormSubmissionResponse;
import com.TuWebYa.horno.forms.application.port.in.CheckFormExistsUseCase;
import com.TuWebYa.horno.forms.application.port.in.CreateFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.SubmitFormUseCase;
import com.TuWebYa.horno.hosting.application.port.in.CreateProjectUseCase;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class SubmitFormUseCaseImpl implements SubmitFormUseCase {
    
    private final CheckFormExistsUseCase checkFormExistsUseCase;
    private final CreateFormUseCase createFormUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final CreateProjectUseCase createProjectUseCase;
    private final PasswordGeneratorService passwordGeneratorService;
    
    public SubmitFormUseCaseImpl(
            CheckFormExistsUseCase checkFormExistsUseCase,
            CreateFormUseCase createFormUseCase,
            CreateUserUseCase createUserUseCase,
            CreateProjectUseCase createProjectUseCase,
            PasswordGeneratorService passwordGeneratorService) {
        this.checkFormExistsUseCase = checkFormExistsUseCase;
        this.createFormUseCase = createFormUseCase;
        this.createUserUseCase = createUserUseCase;
        this.createProjectUseCase = createProjectUseCase;
        this.passwordGeneratorService = passwordGeneratorService;
    }
    
    @Override
    public Mono<FormSubmissionResponse> submitForm(CreateFormRequest request) {
        // Verificar si el email ya existe
        return checkFormExistsUseCase.existsByEmail(request.email())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        // Email existe - el frontend debe redirigir al login
                        return Mono.just(new FormSubmissionResponse(
                                "Email already exists. Please login.",
                                false,
                                null,
                                true,
                                false
                        ));
                    }
                    
                    // Email no existe - crear usuario, formulario y proyecto
                    String temporaryPassword = passwordGeneratorService.generateRandomPassword();
                    
                    CreateUserCommand createUserCommand = new CreateUserCommand(
                            request.email(),
                            temporaryPassword,
                            "USER",
                            "PUBLIC" // Para permitir creación desde formulario público
                    );
                    
                    return createUserUseCase.createUser(createUserCommand)
                            .flatMap(userResponse -> {
                                UUID userId = UUID.fromString(userResponse.id());
                                
                                com.TuWebYa.horno.forms.application.command.CreateFormCommand createFormCommand =
                                        new com.TuWebYa.horno.forms.application.command.CreateFormCommand(
                                                request.email(),
                                                request.businessName(),
                                                request.businessInfo(),
                                                request.objectives(),
                                                request.preferences(),
                                                userId,
                                                null // No asignar admin desde formulario público
                                        );
                                
                                return createFormUseCase.createForm(createFormCommand)
                                        .flatMap(formResponse -> {
                                            // Crear proyecto después de guardar el formulario
                                            return createProjectUseCase.createProject(formResponse.id(), userId)
                                                    .then(Mono.just(new FormSubmissionResponse(
                                                            "Form submitted successfully",
                                                            true,
                                                            temporaryPassword,
                                                            false,
                                                            true // Proyecto creado
                                                    )));
                                        });
                            });
                });
    }
}

