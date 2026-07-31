package com.govtech.connectors.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.spi.DocumentSourceConnector;
import com.govtech.connectors.domain.port.DocumentRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscoverDocumentsUseCase {

    private final PublishDocumentUseCase publisher;

    private final DocumentRegistry registry;

    public void execute(
            DocumentSourceConnector connector) {

        log.info(
                "Discovering documents from {}",
                connector.name());

        List<ConnectorDocument> documents = connector.discover();

        int published = 0;

        for (ConnectorDocument document : documents) {

            if (isAlreadyProcessed(document)) {

                log.debug(
                        "Document already processed {}",
                        document.id());

                continue;

            }

            registry.save(document);

            publisher.publish(document);

            published++;

        }

        log.info(
                "{} documents discovered, {} published from {}",
                documents.size(),
                published,
                connector.name());

    }

    private boolean isAlreadyProcessed(
            ConnectorDocument document) {

        return document.checksum() != null
                && registry.exists(
                        document.checksum());

    }

}