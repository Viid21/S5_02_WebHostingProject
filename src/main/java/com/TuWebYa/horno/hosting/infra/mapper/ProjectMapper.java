package com.TuWebYa.horno.hosting.infra.mapper;

import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.hosting.domain.model.Project;
import com.TuWebYa.horno.hosting.domain.model.ProjectId;
import com.TuWebYa.horno.hosting.domain.model.ProjectStatus;
import com.TuWebYa.horno.hosting.infra.entity.ProjectEntity;
import com.TuWebYa.horno.user.domain.model.UserId;

public class ProjectMapper {
    
    public static ProjectEntity toEntity(Project project) {
        ProjectEntity entity = new ProjectEntity();
        if (project.getId() != null) {
            entity.setId(project.getId().toString());
        }
        entity.setFormId(project.getFormId().toString());
        entity.setUserId(project.getUserId().value());
        entity.setStatus(project.getStatus().name());
        entity.setDescription(project.getDescription());
        if (project.getAssignedAdminId() != null) {
            entity.setAssignedAdminId(project.getAssignedAdminId().value());
        }
        entity.setCreatedAt(project.getCreatedAt());
        entity.setUpdatedAt(project.getUpdatedAt());
        return entity;
    }
    
    public static Project toDomain(ProjectEntity entity) {
        Project project = new Project(
                FormId.from(entity.getFormId()),
                UserId.from(entity.getUserId().toString()),
                ProjectStatus.valueOf(entity.getStatus())
        );
        project.setId(ProjectId.from(entity.getId()));
        project.setDescription(entity.getDescription());
        if (entity.getAssignedAdminId() != null) {
            project.setAssignedAdminId(UserId.from(entity.getAssignedAdminId().toString()));
        }
        project.setCreatedAt(entity.getCreatedAt());
        project.setUpdatedAt(entity.getUpdatedAt());
        return project;
    }
}

