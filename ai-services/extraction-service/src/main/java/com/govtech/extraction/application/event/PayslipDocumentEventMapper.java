package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.payslip.PayslipExtractedData;
import com.govtech.events.payslip.PayslipExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class PayslipDocumentEventMapper
        extends AbstractDocumentEventMapper<PayslipExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.PAYSLIP;
    }

    @Override
    public PayslipExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            PayslipExtractedData data = objectMapper.readValue(extractedData, PayslipExtractedData.class);

            return PayslipExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}