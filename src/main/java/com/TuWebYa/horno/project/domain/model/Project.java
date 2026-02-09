package com.TuWebYa.horno.project.domain.model;

import com.TuWebYa.horno.forms.domain.model.FormId;
import com.TuWebYa.horno.user.domain.model.UserId;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Project {
    private ProjectId id;
    @NonNull
    private FormId formId;
    @NonNull
    private UserId userId;
    @NonNull
    private ProjectStatus status;
    private String description;
    private UserId assignedAdminId; // Admin asignado
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static Project create(FormId formId, UserId userId) {
        Project project = new Project(formId, userId, ProjectStatus.PENDING);
        project.setId(ProjectId.generate());
        project.setCreatedAt(LocalDateTime.now());
        project.setUpdatedAt(LocalDateTime.now());
        return project;
    }
    
    public void updateStatus(ProjectStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void assignAdmin(UserId adminId) {
        this.assignedAdminId = adminId;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Project project = (Project) object;
        return Objects.equals(id, project.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

