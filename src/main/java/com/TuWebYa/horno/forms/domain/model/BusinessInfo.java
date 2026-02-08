package com.TuWebYa.horno.forms.domain.model;

import java.util.Objects;

public class BusinessInfo {
    private static final int MAX_LENGTH = 5000;
    
    private final String value;
    
    private BusinessInfo(String value) {
        this.value = value;
    }
    
    public static BusinessInfo of(String rawInfo) {
        if (rawInfo == null) {
            rawInfo = "";
        }
        
        String info = rawInfo.trim();
        
        if (info.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Business info must be at most " + MAX_LENGTH + " characters");
        }
        
        return new BusinessInfo(info);
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        BusinessInfo that = (BusinessInfo) object;
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

