package com.TuWebYa.horno.hosting.application.port.in;

import com.TuWebYa.horno.hosting.application.dto.response.ProjectResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CreateProjectUseCase {
    Mono<ProjectResponse> createProject(String formId, UUID userId);
}

