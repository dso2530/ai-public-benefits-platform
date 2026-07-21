package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.rentreceipt.RentReceiptExtractedData;
import com.govtech.events.rentreceipt.RentReceiptExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class RentReceiptDocumentEventMapper
        extends AbstractDocumentEventMapper<RentReceiptExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.RENT_RECEIPT;
    }

    @Override
    public RentReceiptExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            RentReceiptExtractedData data = objectMapper.readValue(
                    extractedData,
                    RentReceiptExtractedData.class);

            return RentReceiptExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}