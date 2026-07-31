package com.govtech.connectors.common.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record ConnectorMetadata(

                String connectorName,

                String source,

                String version,

                Instant timestamp,

                String correlationId,

                Map<String, String> attributes

) {

        public ConnectorMetadata {

                if (connectorName == null || connectorName.isBlank()) {
                        throw new IllegalArgumentException(
                                        "connectorName must not be blank");
                }

                if (timestamp == null) {
                        timestamp = Instant.now();
                }

                if (correlationId == null || correlationId.isBlank()) {
                        correlationId = UUID.randomUUID().toString();
                }

                if (attributes == null) {
                        attributes = Map.of();
                }

        }

        public static ConnectorMetadata of(
                        String connectorName,
                        String source) {

                return new ConnectorMetadata(

                                connectorName,

                                source,

                                null,

                                Instant.now(),

                                UUID.randomUUID().toString(),

                                Map.of()

                );

        }

}