package com.TuWebYa.horno.project.infra.adapter;

import com.TuWebYa.horno.project.application.port.out.ProjectRepositoryPort;
import com.TuWebYa.horno.project.domain.model.Project;
import com.TuWebYa.horno.project.infra.mapper.ProjectMapper;
import com.TuWebYa.horno.project.infra.repository.ProjectMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ProjectRepositoryImpl implements ProjectRepositoryPort {
    
    private final ProjectMongoRepository repository;
    
    public ProjectRepositoryImpl(ProjectMongoRepository repository) {
        this.repository = repository;
    }
    
    @Override
    public Mono<Project> save(Project project) {
        return repository.save(ProjectMapper.toEntity(project))
                .map(ProjectMapper::toDomain);
    }
    
    @Override
    public Mono<Project> findByFormId(String formId) {
        return repository.findByFormId(formId)
                .map(ProjectMapper::toDomain);
    }
    
    @Override
    public Mono<Project> findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .map(ProjectMapper::toDomain);
    }
}

