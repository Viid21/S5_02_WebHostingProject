package com.TuWebYa.horno.forms.domain.model;

import java.util.Objects;

public class Objectives {
    private static final int MAX_LENGTH = 2000;
    
    private final String value;
    
    private Objectives(String value) {
        this.value = value;
    }
    
    public static Objectives of(String rawObjectives) {
        if (rawObjectives == null) {
            rawObjectives = "";
        }
        
        String objectives = rawObjectives.trim();
        
        if (objectives.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Objectives must be at most " + MAX_LENGTH + " characters");
        }
        
        return new Objectives(objectives);
    }
    
    public String value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Objectives that = (Objectives) object;
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

