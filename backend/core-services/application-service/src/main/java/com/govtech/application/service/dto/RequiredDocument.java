package com.govtech.application.service.dto;

import com.govtech.application.domain.model.DocumentType;

public record RequiredDocument(
                DocumentType type,
                String label,
                boolean required) {
}