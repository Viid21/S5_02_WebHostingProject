package com.TuWebYa.horno.forms.domain.model;

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
public class Form {
    private FormId id;
    @NonNull
    private com.TuWebYa.horno.user.domain.model.UserEmail email;
    @NonNull
    private BusinessName businessName;
    @NonNull
    private BusinessInfo businessInfo;
    @NonNull
    private Objectives objectives;
    @NonNull
    private Preferences preferences;
    private UserId userId; // Usuario asociado (puede ser null si se crea desde formulario)
    private UserId assignedAdminId; // Admin asignado (solo si es superadmin)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static Form create(
            com.TuWebYa.horno.user.domain.model.UserEmail email,
            BusinessName businessName,
            BusinessInfo businessInfo,
            Objectives objectives,
            Preferences preferences,
            UserId userId) {
        Form form = new Form(email, businessName, businessInfo, objectives, preferences);
        form.setId(FormId.generate());
        form.setUserId(userId);
        form.setCreatedAt(LocalDateTime.now());
        form.setUpdatedAt(LocalDateTime.now());
        return form;
    }
    
    public void update(
            BusinessName businessName,
            BusinessInfo businessInfo,
            Objectives objectives,
            Preferences preferences) {
        this.businessName = businessName;
        this.businessInfo = businessInfo;
        this.objectives = objectives;
        this.preferences = preferences;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void assignAdmin(UserId adminId) {
        this.assignedAdminId = adminId;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Form form = (Form) object;
        return Objects.equals(id, form.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

