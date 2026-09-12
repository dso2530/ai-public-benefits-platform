package com.govtech.connectors.application.usecase;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentIngestedResult;
import com.govtech.connectors.common.spi.DocumentSourceConnector;
import com.govtech.connectors.domain.port.DocumentRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiscoverDocumentsUseCase {

        private final IngestExternalDocumentUseCase ingestionUseCase;

        private final DocumentRegistry registry;

        public void execute(
                        DocumentSourceConnector connector) {

                log.info(
                                "Discovering documents from {}",
                                connector.name());

                List<ConnectorDocument> documents = connector.discover();

                int ingested = 0;

                for (ConnectorDocument document : documents) {
                        if (registry.existsByExternalId(document.id())) {

                                log.debug(
                                                "Document already ingested externalId={}",
                                                document.id());

                                continue;
                        }
                        try {

                                DocumentIngestedResult result = ingestionUseCase.execute(document);

                                log.info(
                                                "Ingest result externalId={}, checksum={}, objectKey={}",
                                                result.externalId(),
                                                result.checksum(),
                                                result.objectKey());

                                registry.save(

                                                result.externalId(),

                                                result.checksum(),

                                                document.source()

                                );

                                ingested++;

                        } catch (Exception e) {

                                log.error(
                                                "Unable to ingest document {}",
                                                document.id(),
                                                e);

                        }

                }

                log.info(
                                "{} documents discovered, {} ingested from {}",
                                documents.size(),
                                ingested,
                                connector.name());

        }

}