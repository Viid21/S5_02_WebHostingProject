package com.TuWebYa.horno.hosting.infra.repository;

import com.TuWebYa.horno.hosting.infra.entity.ProjectEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProjectMongoRepository extends ReactiveMongoRepository<ProjectEntity, String> {
    Mono<ProjectEntity> findByFormId(String formId);
    Mono<ProjectEntity> findByUserId(UUID userId);
}

