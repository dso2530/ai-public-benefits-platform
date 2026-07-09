package com.govtech.extraction.application.mapper;

import org.springframework.stereotype.Component;

import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

@Component
public class DocumentMapper {

    public Document toDomain(DocumentOCRCompletedEvent event) {

        return new Document(
                event.getDocumentId(),
                event.getSubject(),
                event.getBucket(),
                event.getObjectKey(),
                event.getContentType(),
                DocumentType.from(event.getDocumentType()),
                event.getText());
    }
}