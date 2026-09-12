
package com.govtech.security.application.event;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.common.DocumentType;
import com.govtech.events.security.DocumentScanCompletedEvent;
import com.govtech.platform.messaging.event.EventContext;
import com.govtech.security.domain.model.SecurityScan;

@Component
public class DocumentScanEventFactory {

        private static final String PRODUCER = "security-service";

        /**
         * Construit l'événement DocumentScanCompleted.
         *
         * eventId = nouveau UUID
         * correlationId = récupéré depuis l'événement parent
         * causationId = eventId de DocumentScanRequested
         */
        public DocumentScanCompletedEvent buildScanCompleted(
                        SecurityScan scan,
                        Map<String, String> metadata,
                        EventContext eventContext) {

                String eventId = UUID.randomUUID().toString();

                return DocumentScanCompletedEvent.newBuilder()
                                .setBaseEvent(
                                                buildDocumentBase(
                                                                scan,
                                                                eventId,
                                                                eventContext))
                                .setSecurityStatus(
                                                scan.getStatus().name())
                                .setScanEngine(
                                                scan.getScanEngine())
                                .setScannedAt(
                                                scan.getScannedAt().toString())
                                .setDetectedContentType(
                                                scan.getDetectedContentType())
                                .setMetadata(
                                                metadata != null
                                                                ? metadata
                                                                : Map.of())
                                .build();
        }

        /**
         * Construction du DocumentBaseEvent commun.
         */
        private DocumentBaseEvent buildDocumentBase(
                        SecurityScan scan,
                        String eventId,
                        EventContext eventContext) {

                BaseEvent baseEvent = BaseEvent.newBuilder()
                                .setEventId(
                                                eventId)
                                .setOccurredAt(
                                                scan.getScannedAt().toString())
                                .setCorrelationId(
                                                eventContext.correlationId())
                                .setCausationId(
                                                eventContext.causationId())
                                .setProducer(
                                                PRODUCER)
                                .build();

                return DocumentBaseEvent.newBuilder()
                                .setBase(
                                                baseEvent)

                                .setDocumentId(
                                                scan.getDocumentId())

                                .setOrigin(
                                                toAvroDocumentOrigin(
                                                                scan.getOrigin()))

                                .setSha256(
                                                scan.getSha256())

                                .setBucket(scan.getBucket())

                                .setObjectKey(scan.getObjectKey())

                                .setContentType(scan.getContentType())

                                .setDocumentType(DocumentType.valueOf(scan.getDocumentType().name()))

                                .setFileName(scan.getFileName())

                                .build();
        }

        private DocumentOrigin toAvroDocumentOrigin(
                        com.govtech.shared.model.DocumentOrigin origin) {

                if (origin == null) {
                        return null;
                }

                return DocumentOrigin.valueOf(
                                origin.name());
        }

}
