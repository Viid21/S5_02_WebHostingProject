package com.TuWebYa.horno.hosting.domain.model;

import org.bson.types.ObjectId;

import java.util.Objects;

public class ProjectId {
    private final ObjectId value;
    
    private ProjectId(ObjectId value) {
        this.value = value;
    }
    
    public static ProjectId generate() {
        return new ProjectId(new ObjectId());
    }
    
    public static ProjectId from(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Project ID cannot be null or empty");
        }
        return new ProjectId(new ObjectId(id));
    }
    
    public static ProjectId from(ObjectId objectId) {
        if (objectId == null) {
            throw new IllegalArgumentException("ObjectId cannot be null");
        }
        return new ProjectId(objectId);
    }
    
    public ObjectId value() {
        return value;
    }
    
    public String toString() {
        return value.toHexString();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ProjectId projectId = (ProjectId) object;
        return Objects.equals(value, projectId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

