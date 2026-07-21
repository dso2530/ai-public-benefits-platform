package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.caf.CafCertificateExtractionCompletedEvent;
import com.govtech.events.caf.CafExtractedData;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class CafCertificateDocumentEventMapper
        extends AbstractDocumentEventMapper<CafCertificateExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.CAF_CERTIFICATE;
    }

    @Override
    public CafCertificateExtractionCompletedEvent toEvent(
            Document document,
            String extractedData,
            String model) {

        try {

            CafExtractedData data = objectMapper.readValue(extractedData, CafExtractedData.class);

            return CafCertificateExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}