package com.govtech.document.application.mapper;

import com.govtech.document.api.dto.DocumentDto;
import com.govtech.document.infrastructure.persistence.DocumentJpaEntity;
import lombok.NonNull;
import org.springframework.stereotype.Component;

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
        entity.getUploadedAt());
  }
}
