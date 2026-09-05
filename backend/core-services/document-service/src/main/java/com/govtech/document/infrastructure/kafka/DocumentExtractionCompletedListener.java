package com.govtech.document.infrastructure.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.govtech.events.common.DocumentOrigin;
import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.platform.messaging.idempotency.IdempotencyService;
import com.govtech.platform.messaging.publisher.EventPublisher;
import com.govtech.platform.messaging.topics.Topics;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentExtractionCompletedListener {

    private final EventPublisher publisher;

    private final IdempotencyService idempotencyService;

    @Value("${messaging.kafka.group-id}")
    private String consumerGroup;

    @KafkaListener(topics = Topics.DOCUMENT_EXTRACTION_COMPLETED, groupId = "${messaging.kafka.group-id}")
    public void consume(
            DocumentExtractionCompletedEvent event) {

        var baseEvent = event.getBaseEvent();
        var eventMetadata = baseEvent.getBase();

        Long documentId = baseEvent.getDocumentId();

        String eventId = eventMetadata.getEventId();

        DocumentOrigin origin = baseEvent.getOrigin();

        log.info(
                "Extraction completed documentId={} eventId={} origin={}",
                documentId,
                eventId,
                origin);

        if (eventId == null || eventId.isBlank()) {
            throw new IllegalStateException(
                    "Event ID is missing for DocumentExtractionCompletedEvent "
                            + "documentId=" + documentId);
        }

        /*
         * Idempotence du routeur.
         *
         * Même eventId + même consumerGroup
         * => l'événement ne sera routé qu'une seule fois.
         */
        if (!idempotencyService.tryAcquire(
                consumerGroup,
                eventId)) {

            log.info(
                    "Duplicate DocumentExtractionCompletedEvent ignored "
                            + "documentId={} eventId={} consumerGroup={}",
                    documentId,
                    eventId,
                    consumerGroup);

            return;
        }

        try {

            if (origin == null) {
                throw new IllegalStateException(
                        "Document origin is missing "
                                + "for documentId="
                                + documentId
                                + " eventId="
                                + eventId);
            }

            switch (origin) {

                case USER_UPLOAD -> {

                    publisher.publish(
                            Topics.DOCUMENT_USER_EXTRACTION_COMPLETED,
                            documentId.toString(),
                            event);
                }

                case CONNECTOR -> {

                    publisher.publish(
                            Topics.DOCUMENT_EXTERNAL_EXTRACTION_COMPLETED,
                            documentId.toString(),
                            event);
                }

                case SYSTEM_GENERATED -> {

                    log.warn(
                            "No extraction route configured for system generated "
                                    + "documentId={} eventId={}",
                            documentId,
                            eventId);
                }

                default -> throw new IllegalStateException(
                        "Unsupported document origin "
                                + origin
                                + " documentId="
                                + documentId
                                + " eventId="
                                + eventId);
            }

            /*
             * On marque PROCESSED uniquement après
             * un routage réussi.
             */
            idempotencyService.markProcessed(
                    consumerGroup,
                    eventId);

        } catch (Exception e) {

            /*
             * Le routage a échoué :
             * on permet au retry Kafka de reprendre.
             */
            idempotencyService.release(
                    consumerGroup,
                    eventId);

            throw e;
        }
    }
}