package com.TuWebYa.horno.forms.infra.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "forms")
public class FormEntity {
    @Id
    private String id;
    private String email;
    private String businessName;
    private String businessInfo;
    private String objectives;
    private String preferences;
    private UUID userId;
    private UUID assignedAdminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBusinessName() {
        return businessName;
    }
    
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    
    public String getBusinessInfo() {
        return businessInfo;
    }
    
    public void setBusinessInfo(String businessInfo) {
        this.businessInfo = businessInfo;
    }
    
    public String getObjectives() {
        return objectives;
    }
    
    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }
    
    public String getPreferences() {
        return preferences;
    }
    
    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }
    
    public UUID getUserId() {
        return userId;
    }
    
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    
    public UUID getAssignedAdminId() {
        return assignedAdminId;
    }
    
    public void setAssignedAdminId(UUID assignedAdminId) {
        this.assignedAdminId = assignedAdminId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

