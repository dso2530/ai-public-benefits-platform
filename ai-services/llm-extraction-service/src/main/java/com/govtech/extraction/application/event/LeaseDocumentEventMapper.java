package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.lease.LeaseExtractedData;
import com.govtech.events.lease.LeaseExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;
import com.govtech.platform.messaging.event.EventContext;

import tools.jackson.databind.ObjectMapper;

@Component
public class LeaseDocumentEventMapper
        extends AbstractDocumentEventMapper<LeaseExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.LEASE;
    }

    @Override
    public LeaseExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model,
            EventContext eventContext) {

        try {

            LeaseExtractedData data = objectMapper.readValue(
                    extractedData,
                    LeaseExtractedData.class);

            return LeaseExtractionCompletedEvent.newBuilder()
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