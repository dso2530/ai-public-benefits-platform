package com.govtech.extraction.application.event;

import org.springframework.stereotype.Component;

import tools.jackson.databind.ObjectMapper;
import com.govtech.events.tax.TaxNoticeExtractedData;
import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TaxNoticeEventMapper
                extends AbstractDocumentEventMapper<TaxNoticeExtractionCompletedEvent> {

        private final ObjectMapper objectMapper;

        @Override
        public DocumentType supports() {
                return DocumentType.TAX_NOTICE;
        }

        @Override
        public TaxNoticeExtractionCompletedEvent toEvent(
                        Document document,
                        String extractedData,
                        String model) {

                try {
                        TaxNoticeExtractedData extraction = objectMapper.readValue(
                                        extractedData,
                                        TaxNoticeExtractedData.class);

                        return TaxNoticeExtractionCompletedEvent.newBuilder()
                                        .setMetadata(buildMetadata(document, model))
                                        .setExtractedData(extraction)
                                        .build();

                } catch (Exception e) {
                        throw new RuntimeException(
                                        "Unable to deserialize TaxNoticeExtractedData",
                                        e);
                }
        }
}