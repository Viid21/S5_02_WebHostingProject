package com.TuWebYa.horno.forms.domain.model;

import org.bson.types.ObjectId;

import java.util.Objects;

public class FormId {
    private final ObjectId value;
    
    private FormId(ObjectId value) {
        this.value = value;
    }
    
    public static FormId generate() {
        return new FormId(new ObjectId());
    }
    
    public static FormId from(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Form ID cannot be null or empty");
        }
        return new FormId(new ObjectId(id));
    }
    
    public static FormId from(ObjectId objectId) {
        if (objectId == null) {
            throw new IllegalArgumentException("ObjectId cannot be null");
        }
        return new FormId(objectId);
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
        FormId formId = (FormId) object;
        return Objects.equals(value, formId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

