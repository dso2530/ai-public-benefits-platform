package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.bank.RibExtractedData;
import com.govtech.events.bank.RibExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class RibDocumentEventMapper
        extends AbstractDocumentEventMapper<RibExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.RIB;
    }

    @Override
    public RibExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            RibExtractedData data = objectMapper.readValue(extractedData, RibExtractedData.class);

            return RibExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}