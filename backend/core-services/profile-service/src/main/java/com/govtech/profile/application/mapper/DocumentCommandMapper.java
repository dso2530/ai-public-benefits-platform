package com.govtech.profile.application.mapper;

import org.springframework.stereotype.Component;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.profile.application.dto.DocumentCommand;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

@Component
public class DocumentCommandMapper {

        public DocumentCommand toCommand(
                        DocumentBaseEvent metadata) {

                return DocumentCommand.builder()
                                .documentId(metadata.getDocumentId())
                                .bucket(metadata.getBucket())
                                .objectKey(metadata.getObjectKey())
                                .contentType(metadata.getContentType())
                                .fileName(metadata.getFileName())
                                .fileSize(metadata.getFileSize())
                                .sha256(metadata.getSha256())
                                .documentType(
                                                DocumentType.valueOf(metadata.getDocumentType().name()))
                                .origin(DocumentOrigin.valueOf(
                                                metadata.getOrigin().name()))
                                .build();
        }
}