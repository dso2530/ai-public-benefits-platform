package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.homeinsurance.HomeInsuranceExtractedData;
import com.govtech.events.homeinsurance.HomeInsuranceExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class HomeInsuranceDocumentEventMapper
        extends AbstractDocumentEventMapper<HomeInsuranceExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.HOME_INSURANCE;
    }

    @Override
    public HomeInsuranceExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            HomeInsuranceExtractedData data =
                    objectMapper.readValue(
                            extractedData,
                            HomeInsuranceExtractedData.class);

            return HomeInsuranceExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}