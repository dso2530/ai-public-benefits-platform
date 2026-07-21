package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.passport.PassportExtractedData;
import com.govtech.events.passport.PassportExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class PassportDocumentEventMapper
        extends AbstractDocumentEventMapper<PassportExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.PASSPORT;
    }

    @Override
    public PassportExtractionCompletedEvent toEvent(Document document, String extractedData, String model) {

        try {

            PassportExtractedData data = objectMapper.readValue(extractedData, PassportExtractedData.class);

            return PassportExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}