package com.TuWebYa.horno.project.application.port.out;

import com.TuWebYa.horno.project.domain.model.Project;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProjectRepositoryPort {
    Mono<Project> save(Project project);
    Mono<Project> findByFormId(String formId);
    Mono<Project> findByUserId(UUID userId);
}

