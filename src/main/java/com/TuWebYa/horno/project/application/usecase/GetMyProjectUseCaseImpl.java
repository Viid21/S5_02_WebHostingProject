package com.TuWebYa.horno.project.application.usecase;

import com.TuWebYa.horno.project.application.dto.response.ProjectResponse;
import com.TuWebYa.horno.project.application.exception.ProjectNotFoundException;
import com.TuWebYa.horno.project.application.port.in.GetMyProjectUseCase;
import com.TuWebYa.horno.project.application.port.out.ProjectRepositoryPort;
import com.TuWebYa.horno.project.domain.model.Project;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class GetMyProjectUseCaseImpl implements GetMyProjectUseCase {

    private final ProjectRepositoryPort projectRepositoryPort;

    public GetMyProjectUseCaseImpl(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }

    @Override
    public Mono<ProjectResponse> getMyProject(UUID userId) {
        return projectRepositoryPort.findByUserId(userId)
                .switchIfEmpty(Mono.error(new ProjectNotFoundException("Project not found for user: " + userId)))
                .map(this::toResponse);
    }

    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId().toString(),
                project.getFormId().toString(),
                project.getUserId().value(),
                project.getStatus().name(),
                project.getDescription(),
                project.getAssignedAdminId().value(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}

