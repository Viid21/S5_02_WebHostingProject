package com.TuWebYa.horno.forms.application.dto.request;

public record CreateFormRequest(
        String email,
        String businessName,
        String businessInfo,
        String objectives,
        String preferences
) {
}

