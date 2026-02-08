package com.TuWebYa.horno.forms.domain.model;

import java.util.Objects;

public class BusinessName {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    
    private final String value;
    
    private BusinessName(String value) {
        this.value = value;
    }
    
    public static BusinessName of(String rawName) {
        if (rawName == null || rawName.trim().isEmpty()) {
            throw new IllegalArgumentException("Business name cannot be empty");
        }
        
        String name = rawName.trim();
        
        if (name.length() < MIN_LENGTH) {
            throw new IllegalArgumentException("Business name must be at least " + MIN_LENGTH + " characters");
        }
        
        if (name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Business name must be at most " + MAX_LENGTH + " characters");
        }
        
        return new BusinessName(name);
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        BusinessName that = (BusinessName) object;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
}

