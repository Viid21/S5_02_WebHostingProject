package com.TuWebYa.horno.forms.infra.repository;

import com.TuWebYa.horno.forms.infra.entity.FormEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FormMongoRepository extends ReactiveMongoRepository<FormEntity, String> {
    Mono<FormEntity> findByEmail(String email);
    Mono<FormEntity> findByUserId(java.util.UUID userId);
    Mono<Boolean> existsByEmail(String email);
}

