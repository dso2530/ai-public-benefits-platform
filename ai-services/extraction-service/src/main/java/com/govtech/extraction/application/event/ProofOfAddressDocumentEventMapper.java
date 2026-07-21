package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import com.govtech.events.proofofaddress.ProofOfAddressExtractedData;
import com.govtech.events.proofofaddress.ProofOfAddressExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import tools.jackson.databind.ObjectMapper;

@Component
public class ProofOfAddressDocumentEventMapper
        extends AbstractDocumentEventMapper<ProofOfAddressExtractionCompletedEvent> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DocumentType supports() {
        return DocumentType.PROOF_OF_ADDRESS;
    }

    @Override
    public ProofOfAddressExtractionCompletedEvent toEvent(Document document, String extractedData, String model) {

        try {

            ProofOfAddressExtractedData data = objectMapper.readValue(extractedData, ProofOfAddressExtractedData.class);

            return ProofOfAddressExtractionCompletedEvent.newBuilder()
                    .setMetadata(buildMetadata(document, model))
                    .setExtractedData(data)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Unable to map extracted data", e);
        }
    }
}