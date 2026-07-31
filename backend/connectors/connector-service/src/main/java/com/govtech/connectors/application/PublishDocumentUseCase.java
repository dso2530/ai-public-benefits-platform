package com.govtech.connectors.application;

import org.springframework.stereotype.Service;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublishDocumentUseCase {

    private final EventPublisher publisher;

    public void publish(
            ConnectorDocument document) {

        publisher.publish(
                "document.discovered",
                document.id(),
                document)

                .exceptionally(ex -> {

                    log.error(
                            "Document publication failed",
                            ex);

                    return null;

                });

    }

}