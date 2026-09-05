package com.govtech.extraction.application.event;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentOrigin;
import com.govtech.extraction.domain.model.DocumentType;

@Component
public class DocumentMapper {

    public Document toDomain(DocumentExtractionCompletedEvent event) {

        return new Document(
                event.getBaseEvent().getDocumentId(),
                event.getBaseEvent().getBase().getSubject(),
                event.getBaseEvent().getBucket(),
                event.getBaseEvent().getObjectKey(),
                event.getBaseEvent().getContentType(),
                DocumentType.from(event.getBaseEvent().getDocumentType().name()),
                DocumentOrigin.valueOf(event.getBaseEvent().getOrigin().name()),
                event.getText(),
                null != event.getBaseEvent().getApplicationId()
                        ? UUID.fromString(event.getBaseEvent().getApplicationId())
                        : null);
    }
}