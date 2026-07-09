package com.govtech.extraction.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.tax.TaxNoticeExtractedData;
import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;
import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class TaxNoticeEventMapper
                implements DocumentEventMapper<TaxNoticeExtractionCompletedEvent> {

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

                TaxNoticeExtractedData extraction = objectMapper.readValue(extractedData, TaxNoticeExtractedData.class);

                DocumentBaseEvent metadata = DocumentBaseEvent.newBuilder()
                                .setEventId(UUID.randomUUID().toString())
                                .setOccurredAt(Instant.now().toString())
                                .setDocumentId(document.documentId())
                                .setSubject(document.subject())
                                .setBucket(document.bucket())
                                .setObjectKey(document.objectKey())
                                .setContentType(document.contentType())
                                .setDocumentType(com.govtech.events.common.DocumentType.valueOf(document.type().name()))
                                .setModel(model)

                                .build();

                return TaxNoticeExtractionCompletedEvent.newBuilder()
                                .setMetadata(metadata)
                                .setExtractedData(extraction)
                                .build();

        }
}