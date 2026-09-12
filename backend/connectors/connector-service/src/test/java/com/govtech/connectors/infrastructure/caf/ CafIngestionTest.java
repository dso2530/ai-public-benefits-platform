package com.govtech.connectors.infrastructure.caf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.connectors.application.usecase.DiscoverDocumentsUseCase;
import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.infrastructure.redis.RedisDocumentRegistry;
import com.govtech.platform.storage.service.StorageService;

@SpringBootTest
@ActiveProfiles("test")
class CafIngestionTest {

        @Autowired
        private RedisDocumentRegistry registry;

        @Autowired
        private DiscoverDocumentsUseCase discover;

        @Autowired
        private CafConnector connector;

        @Autowired
        private StorageService storageService;

        @Test
        void should_ingest_caf_documents() {

                List<ConnectorDocument> documents = connector.discover();

                assertThat(documents)
                                .isNotEmpty();

                documents.forEach(d -> System.out.println(
                                d.id() + " - " + d.uri()));

                discover.execute(connector);

                List<String> objects = storageService.list(
                                "documents-ai-quarantine",
                                "external/");

                assertThat(objects)
                                .isNotEmpty();
        }

        @Test
        void should_not_ingest_twice() {

                ConnectorDocument document = connector.discover()
                                .stream()
                                .findFirst()
                                .orElseThrow();

                discover.execute(connector);

                assertThat(
                                registry.existsByExternalId(
                                                document.id()))
                                .isTrue();

                discover.execute(connector);

                assertThat(
                                registry.existsByExternalId(
                                                document.id()))
                                .isTrue();
        }

        @Test
        void should_not_upload_twice() {

                discover.execute(connector);

                List<String> first = storageService.list(
                                "documents-ai-quarantine",
                                "external/");

                discover.execute(connector);

                List<String> second = storageService.list(
                                "documents-ai-quarantine",
                                "external/");

                assertThat(second)
                                .containsExactlyInAnyOrderElementsOf(first);
        }
}