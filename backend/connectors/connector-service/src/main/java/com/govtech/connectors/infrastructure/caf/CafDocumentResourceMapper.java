package com.govtech.connectors.infrastructure.caf;

import java.net.URI;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentMetadata;
import com.govtech.connectors.common.model.DocumentSourceType;

@Component
public class CafDocumentResourceMapper {

        public ConnectorDocument map(URI uri) {

                String id = uri.toString();

                return new ConnectorDocument(
                                id,
                                uri,
                                "caf",
                                DocumentSourceType.CAF,
                                "FR",
                                null,
                                0L,
                                null,
                                DocumentMetadata.empty(),
                                Instant.now());
        }
}