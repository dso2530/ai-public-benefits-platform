package com.govtech.connectors.infrastructure.caf;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.govtech.connectors.application.usecase.DownloadDocumentUseCase;
import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentContent;

@SpringBootTest
@ActiveProfiles("test")
class CafDownloadTest {

    @Autowired
    private CafConnector connector;

    @Autowired
    private DownloadDocumentUseCase downloadDocumentUseCase;

    @Test
    void should_download_caf_document() {

        ConnectorDocument document = connector.discover()
                .stream()
                .findFirst()
                .orElseThrow();

        DocumentContent content = downloadDocumentUseCase.execute(
                document.uri());

        assertThat(content.content())
                .isNotNull();

        assertThat(content.content().length)
                .isGreaterThan(0);

        assertThat(content.checksum())
                .isNotBlank();

        assertThat(content.contentType())
                .isNotBlank();
    }
}