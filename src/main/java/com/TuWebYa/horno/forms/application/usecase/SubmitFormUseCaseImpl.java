package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.auth.infra.security.JwtService;
import com.TuWebYa.horno.common.service.PasswordGeneratorService;
import com.TuWebYa.horno.forms.application.command.CreateFormCommand;
import com.TuWebYa.horno.forms.application.dto.request.CreateFormRequest;
import com.TuWebYa.horno.forms.application.dto.response.FormSubmissionResponse;
import com.TuWebYa.horno.forms.application.port.in.CheckFormExistsUseCase;
import com.TuWebYa.horno.forms.application.port.in.CreateFormUseCase;
import com.TuWebYa.horno.forms.application.port.in.SubmitFormUseCase;
import com.TuWebYa.horno.project.application.port.in.CreateProjectUseCase;
import com.TuWebYa.horno.user.application.command.CreateUserCommand;
import com.TuWebYa.horno.user.application.port.in.CreateUserUseCase;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
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
    private final JwtService jwtService;
    private final UserRepositoryPort userRepositoryPort;

    public SubmitFormUseCaseImpl(
            CheckFormExistsUseCase checkFormExistsUseCase,
            CreateFormUseCase createFormUseCase,
            CreateUserUseCase createUserUseCase,
            CreateProjectUseCase createProjectUseCase,
            PasswordGeneratorService passwordGeneratorService,
            JwtService jwtService, UserRepositoryPort userRepositoryPort) {
        this.checkFormExistsUseCase = checkFormExistsUseCase;
        this.createFormUseCase = createFormUseCase;
        this.createUserUseCase = createUserUseCase;
        this.createProjectUseCase = createProjectUseCase;
        this.passwordGeneratorService = passwordGeneratorService;
        this.jwtService = jwtService;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    public Mono<FormSubmissionResponse> submitForm(CreateFormRequest request) {
        return checkFormExistsUseCase.existsByEmail(request.email())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return userRepositoryPort.findByEmail(request.email())
                                .flatMap(user -> Mono.just(new FormSubmissionResponse(
                                        "Email already exists. Please login.",
                                        false,
                                        null,
                                        true,
                                        false,
                                        null,
                                        null,
                                        user.getId().toString(),
                                        null,
                                        null
                                )));
                    }

                    String temporaryPassword = passwordGeneratorService.generateRandomPassword();

                    CreateUserCommand createUserCommand = new CreateUserCommand(
                            request.email(),
                            temporaryPassword,
                            "USER",
                            "PUBLIC"
                    );

                    return createUserUseCase.createUser(createUserCommand)
                            .flatMap(userResponse -> {
                                UUID userId = UUID.fromString(userResponse.id());

                                //TODO: Enviar email con contraseña temporal
                                //emailService.sendTemporaryPassword(request.email(), temporaryPassword);

                                CreateFormCommand createFormCommand =
                                        new CreateFormCommand(
                                                request.email(),
                                                request.businessName(),
                                                request.businessInfo(),
                                                request.objectives(),
                                                request.preferences(),
                                                userId,
                                                null
                                        );

                                String accessToken = jwtService.generateAccessToken(userId.toString(), "USER");
                                String refreshToken = jwtService.generateRefreshToken(userId.toString());

                                return createFormUseCase.createForm(createFormCommand)
                                        .flatMap(formResponse -> {

                                            return createProjectUseCase.createProject(formResponse.id(), userId)
                                                    .flatMap(projectId -> Mono.just(new FormSubmissionResponse(
                                                            "Form submitted successfully",
                                                            true,
                                                            temporaryPassword,
                                                            false,
                                                            true,
                                                            accessToken,
                                                            refreshToken,
                                                            userId.toString(),
                                                            formResponse.id(),
                                                            projectId.id()
                                                    )));
                                        });
                            });
                });
    }
}

