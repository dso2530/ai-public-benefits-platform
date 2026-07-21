package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.housingtax.HousingTaxExtractedData;
import com.govtech.events.housingtax.HousingTaxExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class HousingTaxDocumentEventMapper
        extends AbstractDocumentEventMapper<HousingTaxExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.HOUSING_TAX;
    }

    @Override
    public HousingTaxExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            HousingTaxExtractedData data =
                    objectMapper.readValue(extractedData, HousingTaxExtractedData.class);

            return HousingTaxExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}