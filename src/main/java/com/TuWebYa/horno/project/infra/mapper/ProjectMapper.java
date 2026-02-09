package com.TuWebYa.horno.project.infra.mapper;

import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.project.domain.model.Project;
import com.TuWebYa.horno.project.domain.model.ProjectId;
import com.TuWebYa.horno.project.domain.model.ProjectStatus;
import com.TuWebYa.horno.project.infra.document.ProjectDocument;
import com.TuWebYa.horno.user.domain.model.UserId;

public class ProjectMapper {
    
    public static ProjectDocument toEntity(Project project) {
        ProjectDocument entity = new ProjectDocument();
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
    
    public static Project toDomain(ProjectDocument entity) {
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

