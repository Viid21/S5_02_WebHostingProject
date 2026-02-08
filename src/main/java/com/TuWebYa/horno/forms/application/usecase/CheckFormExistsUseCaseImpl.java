package com.TuWebYa.horno.forms.application.usecase;

import com.TuWebYa.horno.forms.application.port.in.CheckFormExistsUseCase;
import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.user.application.port.out.UserRepositoryPort;
import com.TuWebYa.horno.user.domain.model.UserEmail;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CheckFormExistsUseCaseImpl implements CheckFormExistsUseCase {
    
    private final FormRepositoryPort formRepositoryPort;
    private final UserRepositoryPort userRepositoryPort;
    
    public CheckFormExistsUseCaseImpl(
            FormRepositoryPort formRepositoryPort,
            UserRepositoryPort userRepositoryPort) {
        this.formRepositoryPort = formRepositoryPort;
        this.userRepositoryPort = userRepositoryPort;
    }
    
    @Override
    public Mono<Boolean> existsByEmail(String email) {
        // Verificar si existe un usuario con ese email O un formulario con ese email
        UserEmail userEmail = UserEmail.of(email);
        
        Mono<Boolean> userExists = userRepositoryPort.findByEmail(email).hasElement();
        Mono<Boolean> formExists = formRepositoryPort.existsByEmail(userEmail);
        
        return Mono.zip(userExists, formExists)
                .map(tuple -> tuple.getT1() || tuple.getT2());
    }
    
    @Override
    public Mono<Boolean> existsByUserId(UUID userId) {
        return formRepositoryPort.findByUserId(UserId.from(userId.toString()))
                .hasElement();
    }
}

