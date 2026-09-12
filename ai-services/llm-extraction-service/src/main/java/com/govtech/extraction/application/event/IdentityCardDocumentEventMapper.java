package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.identity.IdentityCardExtractedData;
import com.govtech.events.identity.IdentityCardExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;
import com.govtech.platform.messaging.event.EventContext;

import tools.jackson.databind.ObjectMapper;

@Component
public class IdentityCardDocumentEventMapper
        extends AbstractDocumentEventMapper<IdentityCardExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.IDENTITY_CARD;
    }

    @Override
    public IdentityCardExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model,
            EventContext eventContext) {

        try {

            IdentityCardExtractedData data = objectMapper.readValue(
                    extractedData,
                    IdentityCardExtractedData.class);

            return IdentityCardExtractionCompletedEvent.newBuilder()
                    .setMetadata(
                            buildMetadata(
                                    document,
                                    model,
                                    eventContext))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to map extracted data",
                    e);
        }
    }
}