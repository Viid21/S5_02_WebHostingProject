package com.TuWebYa.horno.forms.domain.model;

import java.util.Objects;

public class Preferences {
    private static final int MAX_LENGTH = 2000;
    
    private final String value;
    
    private Preferences(String value) {
        this.value = value;
    }
    
    public static Preferences of(String rawPreferences) {
        if (rawPreferences == null) {
            rawPreferences = "";
        }
        
        String preferences = rawPreferences.trim();
        
        if (preferences.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Preferences must be at most " + MAX_LENGTH + " characters");
        }
        
        return new Preferences(preferences);
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Preferences that = (Preferences) object;
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

