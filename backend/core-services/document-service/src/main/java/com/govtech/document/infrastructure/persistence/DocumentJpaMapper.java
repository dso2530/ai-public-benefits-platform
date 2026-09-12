package com.govtech.document.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.govtech.document.domain.model.Document;

@Component
public class DocumentJpaMapper {

    public Document toDomain(
            DocumentJpaEntity entity) {

        return Document.builder()
                .id(entity.getId())
                .applicationId(entity.getApplicationId())
                .subject(entity.getSubject())
                .bucket(entity.getBucket())
                .objectKey(entity.getObjectKey())
                .contentType(entity.getContentType())
                .documentType(entity.getDocumentType())
                .origin(entity.getOrigin())
                .source(entity.getSource())
                .sha256(entity.getSha256())
                .fileName(entity.getFileName())
                .fileSize(entity.getFileSize())
                .territoryCode(entity.getTerritoryCode())
                .model(null)
                .uploadedAt(entity.getUploadedAt())
                .build();
    }
}