package com.govtech.connectors.common.model;

import java.net.URI;
import java.time.Instant;

public record ConnectorDocument(

                String id,

                URI uri,

                String source,

                DocumentSourceType sourceType,

                String territoryCode,

                String contentType,

                long size,

                String checksum,

                DocumentMetadata metadata,

                Instant discoveredAt

) {

        public ConnectorDocument {

                if (discoveredAt == null) {
                        discoveredAt = Instant.now();
                }

                if (metadata == null) {
                        metadata = DocumentMetadata.empty();
                }

        }

}