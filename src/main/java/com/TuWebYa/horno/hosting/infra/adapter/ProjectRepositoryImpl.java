package com.TuWebYa.horno.hosting.infra.adapter;

import com.TuWebYa.horno.hosting.application.port.out.ProjectRepositoryPort;
import com.TuWebYa.horno.hosting.domain.model.Project;
import com.TuWebYa.horno.hosting.infra.mapper.ProjectMapper;
import com.TuWebYa.horno.hosting.infra.repository.ProjectMongoRepository;
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

