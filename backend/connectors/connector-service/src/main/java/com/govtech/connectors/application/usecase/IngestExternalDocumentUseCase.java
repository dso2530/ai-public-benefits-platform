package com.govtech.connectors.application.usecase;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.govtech.connectors.application.event.PublishDocumentIngestedEventUseCase;
import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.model.DocumentContent;
import com.govtech.connectors.common.model.DocumentIngestedResult;
import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngestExternalDocumentUseCase {

        private static final String QUARANTINE_BUCKET = "documents-ai-quarantine";

        private final DownloadDocumentUseCase download;

        private final StorageService storageService;

        private final PublishDocumentIngestedEventUseCase publisher;

        public DocumentIngestedResult execute(
                        ConnectorDocument document) {

                DocumentContent content = download.execute(
                                document.uri());

                String objectKey = "external/"
                                + document.source()
                                + "/"
                                + UUID.randomUUID()
                                + "/"
                                + content.fileName();

                storageService.upload(
                                new ByteArrayInputStream(content.content()),
                                content.size(),
                                content.contentType(),
                                QUARANTINE_BUCKET,
                                objectKey);

                publisher.publish(
                                document,
                                content,
                                QUARANTINE_BUCKET,
                                objectKey);

                return new DocumentIngestedResult(

                                document.id(),

                                QUARANTINE_BUCKET,

                                objectKey,

                                content.checksum(),

                                content.contentType(),

                                content.size()

                );

        }
}