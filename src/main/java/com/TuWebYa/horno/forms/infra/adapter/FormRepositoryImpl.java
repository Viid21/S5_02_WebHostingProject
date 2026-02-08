package com.TuWebYa.horno.forms.infra.adapter;

import com.TuWebYa.horno.forms.application.port.out.FormRepositoryPort;
import com.TuWebYa.horno.forms.domain.model.Form;
import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.forms.infra.mapper.FormMapper;
import com.TuWebYa.horno.forms.infra.repository.FormMongoRepository;
import com.TuWebYa.horno.user.domain.model.UserEmail;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FormRepositoryImpl implements FormRepositoryPort {
    
    private final FormMongoRepository repository;
    
    public FormRepositoryImpl(FormMongoRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Mono<Form> save(Form form) {
        return repository.save(FormMapper.toEntity(form))
                .map(FormMapper::toDomain);
    }
    
    @Override
    public Mono<Form> findById(FormId formId) {
        return repository.findById(formId.value().toString())
                .map(FormMapper::toDomain);
    }
    
    @Override
    public Mono<Form> findByEmail(UserEmail email) {
        return repository.findByEmail(email.value())
                .map(FormMapper::toDomain);
    }
    
    @Override
    public Mono<Form> findByUserId(UserId userId) {
        return repository.findByUserId(userId.value())
                .map(FormMapper::toDomain);
    }
    
    @Override
    public Mono<Boolean> existsByEmail(UserEmail email) {
        return repository.existsByEmail(email.value());
    }
}

