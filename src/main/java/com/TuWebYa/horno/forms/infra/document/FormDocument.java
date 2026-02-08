package com.TuWebYa.horno.forms.infra.document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "forms")
public class FormDocument {
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
}

