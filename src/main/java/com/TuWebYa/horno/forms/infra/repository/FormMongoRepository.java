package com.TuWebYa.horno.forms.infra.repository;

import com.TuWebYa.horno.forms.infra.document.FormDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FormMongoRepository extends ReactiveMongoRepository<FormDocument, String> {
    Mono<FormDocument> findByEmail(String email);
    Mono<FormDocument> findByUserId(java.util.UUID userId);
    Mono<Boolean> existsByEmail(String email);
}

