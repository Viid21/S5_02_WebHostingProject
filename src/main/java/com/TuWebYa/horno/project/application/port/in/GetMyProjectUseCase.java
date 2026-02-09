package com.TuWebYa.horno.project.application.port.in;

import com.TuWebYa.horno.project.application.dto.response.ProjectResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface GetMyProjectUseCase {
    Mono<ProjectResponse> getMyProject(UUID userId);
}
