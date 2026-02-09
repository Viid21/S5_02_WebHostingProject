package com.TuWebYa.horno.project.infra.repository;

import com.TuWebYa.horno.project.infra.document.ProjectDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProjectMongoRepository extends ReactiveMongoRepository<ProjectDocument, String> {
    Mono<ProjectDocument> findByFormId(String formId);
    Mono<ProjectDocument> findByUserId(UUID userId);
}

