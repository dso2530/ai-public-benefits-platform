package com.govtech.connectors.infrastructure.datagouv;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentMetadata;
import com.govtech.connectors.common.model.DocumentSourceType;

@Component
public class DataGouvResourceMapper {

        public ConnectorDocument map(
                        DataGouvResource resource) {

                return new ConnectorDocument(

                                resource.id(),

                                resource.uri(),

                                "data.gouv.fr",

                                DocumentSourceType.DATAGOUV,

                                "FR",

                                resource.mimeType(),

                                resource.size(),

                                null,

                                new DocumentMetadata(
                                                resource.title(),
                                                null,
                                                "DATASET",
                                                Map.of(
                                                                "format",
                                                                resource.format())),

                                Instant.now()

                );

        }

}