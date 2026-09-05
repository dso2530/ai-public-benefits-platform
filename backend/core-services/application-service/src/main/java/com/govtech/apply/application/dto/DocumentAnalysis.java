package com.govtech.apply.application.dto;

import java.util.List;
import java.util.Map;

import com.govtech.shared.model.DocumentType;

public record DocumentAnalysis(
        Map<DocumentType, StoredDocument> availableDocuments,
        List<DocumentType> missingDocuments) {
}