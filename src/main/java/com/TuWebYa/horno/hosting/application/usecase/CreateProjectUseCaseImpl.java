package com.TuWebYa.horno.hosting.application.usecase;

import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.hosting.application.dto.response.ProjectResponse;
import com.TuWebYa.horno.hosting.application.port.in.CreateProjectUseCase;
import com.TuWebYa.horno.hosting.application.port.out.ProjectRepositoryPort;
import com.TuWebYa.horno.hosting.domain.model.Project;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CreateProjectUseCaseImpl implements CreateProjectUseCase {
    
    private final ProjectRepositoryPort projectRepositoryPort;
    
    public CreateProjectUseCaseImpl(ProjectRepositoryPort projectRepositoryPort) {
        this.projectRepositoryPort = projectRepositoryPort;
    }
    
    @Override
    public Mono<ProjectResponse> createProject(String formId, UUID userId) {
        Project project = Project.create(
                FormId.from(formId),
                UserId.from(userId.toString())
        );
        
        return projectRepositoryPort.save(project)
                .map(this::toResponse);
    }
    
    private ProjectResponse toResponse(Project project) {
        return new ProjectResponse(
                project.getId().toString(),
                project.getFormId().toString(),
                project.getUserId().value(),
                project.getStatus().name(),
                project.getDescription(),
                project.getAssignedAdminId() != null ? project.getAssignedAdminId().value() : null,
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}

