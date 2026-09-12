package com.govtech.connectors.application.event;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentContent;
import com.govtech.events.common.BaseEvent;
import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.common.DocumentType;
import com.govtech.events.connector.ConnectorType;
import com.govtech.events.document.DocumentIngestedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.platform.web.correlation.CorrelationId;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishDocumentIngestedEventUseCase {

        private final EventPublisher publisher;

        public void publish(
                        ConnectorDocument document,
                        DocumentContent content,
                        String bucket,
                        String objectKey) {

                DocumentBaseEvent baseEvent = DocumentBaseEvent.newBuilder()

                                .setBase(
                                                BaseEvent.newBuilder()
                                                                .setEventId(UUID.randomUUID().toString())
                                                                .setOccurredAt(Instant.now().toString())
                                                                .setCorrelationId(CorrelationId.getOrCreate())
                                                                .setCausationId(null)
                                                                .setProducer("connector-service")
                                                                .setSubject("SYSTEM")
                                                                .build())

                                // Le document n'existe pas encore dans document-service
                                .setDocumentId(null)

                                .setApplicationId(null)

                                .setBucket(bucket)

                                .setObjectKey(objectKey)

                                .setContentType(content.contentType())

                                .setDocumentType(DocumentType.OTHER)

                                .setOrigin(DocumentOrigin.CONNECTOR)

                                .setSource(document.source())

                                .setSha256(content.checksum())

                                .setFileName(content.fileName())

                                .setFileSize(content.size())

                                .setModel(null)
                                
                                .setTerritoryCode(document.territoryCode())

                                .build();

                DocumentIngestedEvent event = DocumentIngestedEvent.newBuilder()

                                .setBaseEvent(baseEvent)

                                .setConnectorName(document.source())

                                .setConnectorType(ConnectorType.valueOf(document.sourceType().name()))

                                .setSourceUri(document.uri().toString())

                                .setMetadata(document.metadata().attributes())

                                .build();

                publisher.publish(
                                "document.ingested",
                                document.id(),
                                event)

                                .exceptionally(ex -> {

                                        log.error(
                                                        "Failed to publish DocumentIngestedEvent for {}",
                                                        document.id(),
                                                        ex);

                                        return null;

                                });
        }
}