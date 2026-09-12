package com.govtech.apply.application.dto;

import com.govtech.shared.model.DocumentType;

public record RequiredDocument(
        DocumentType type,
        String label,
        boolean required) {
}