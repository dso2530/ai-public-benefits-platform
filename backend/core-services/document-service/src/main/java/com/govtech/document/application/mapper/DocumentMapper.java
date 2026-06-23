package com.govtech.document.application.mapper;

import org.springframework.stereotype.Component;

import com.govtech.document.application.dto.DocumentDto;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;

import lombok.NonNull;

@Component
public class DocumentMapper {

    public DocumentDto toDto(@NonNull DocumentJpaEntity entity) {
        return new DocumentDto(
                entity.getId(),
                entity.getName(),
                entity.getDocumentType().name(),
                entity.getStatus(),
                entity.getFileName(),
                entity.getFileSize(),
                entity.getUploadedAt()
        );
    }
}