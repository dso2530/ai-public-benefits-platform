package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.energy.EnergyBillExtractedData;
import com.govtech.events.energy.EnergyBillExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class EnergyBillDocumentEventMapper
        extends AbstractDocumentEventMapper<EnergyBillExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.ENERGY_BILL;
    }

    @Override
    public EnergyBillExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            EnergyBillExtractedData data = objectMapper.readValue(
                    extractedData,
                    EnergyBillExtractedData.class);

            return EnergyBillExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}