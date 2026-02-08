package com.TuWebYa.horno.forms.infra.mapper;

import com.TuWebYa.horno.forms.domain.model.*;
import com.TuWebYa.horno.forms.infra.entity.FormEntity;
import com.TuWebYa.horno.user.domain.model.UserEmail;
import com.TuWebYa.horno.user.domain.model.UserId;
import org.bson.types.ObjectId;

import java.util.UUID;

public class FormMapper {
    
    public static FormEntity toEntity(com.TuWebYa.horno.forms.domain.model.Form form) {
        FormEntity entity = new FormEntity();
        
        if (form.getId() != null) {
            entity.setId(form.getId().value().toHexString());
        }
        
        entity.setEmail(form.getEmail().value());
        entity.setBusinessName(form.getBusinessName().value());
        entity.setBusinessInfo(form.getBusinessInfo().value());
        entity.setObjectives(form.getObjectives().value());
        entity.setPreferences(form.getPreferences().value());
        
        if (form.getUserId() != null) {
            entity.setUserId(form.getUserId().value());
        }
        
        if (form.getAssignedAdminId() != null) {
            entity.setAssignedAdminId(form.getAssignedAdminId().value());
        }
        
        entity.setCreatedAt(form.getCreatedAt());
        entity.setUpdatedAt(form.getUpdatedAt());
        
        return entity;
    }
    
    public static com.TuWebYa.horno.forms.domain.model.Form toDomain(FormEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Form form = new Form(
                UserEmail.of(entity.getEmail()),
                BusinessName.of(entity.getBusinessName()),
                BusinessInfo.of(entity.getBusinessInfo()),
                Objectives.of(entity.getObjectives()),
                Preferences.of(entity.getPreferences())
        );
        
        form.setId(FormId.from(new ObjectId(entity.getId())));
        
        if (entity.getUserId() != null) {
            form.setUserId(UserId.from(entity.getUserId().toString()));
        }
        
        if (entity.getAssignedAdminId() != null) {
            form.setAssignedAdminId(UserId.from(entity.getAssignedAdminId().toString()));
        }
        
        form.setCreatedAt(entity.getCreatedAt());
        form.setUpdatedAt(entity.getUpdatedAt());
        
        return form;
    }
}

