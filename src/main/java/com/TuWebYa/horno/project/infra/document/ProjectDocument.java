package com.TuWebYa.horno.project.infra.document;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Document(collection = "projects")
public class ProjectDocument {
    @Id
    private String id;
    private String formId;
    private UUID userId;
    private String status;
    private String description;
    private UUID assignedAdminId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

