package com.govtech.connectors.application.usecase;

import java.net.URI;

import org.springframework.stereotype.Service;

import com.govtech.connectors.common.downloader.ChecksumService;
import com.govtech.connectors.common.downloader.ContentTypeDetector;
import com.govtech.connectors.common.downloader.Downloader;
import com.govtech.connectors.common.model.DocumentContent;
import com.govtech.connectors.common.model.DownloadedResource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DownloadDocumentUseCase {

        private static final String BUCKET = "documents-ai-quarantine";

        private final Downloader downloader;

        private final ContentTypeDetector detector;

        private final ChecksumService checksumService;

        public DocumentContent execute(
                        URI uri) {

                if (uri == null) {
                        throw new IllegalArgumentException(
                                        "Document URI cannot be null");
                }

                try {

                        log.info(
                                        "Downloading document {}",
                                        uri);

                        DownloadedResource resource = downloader.download(uri);

                        if (resource.content() == null
                                        || resource.content().length == 0) {

                                throw new IllegalStateException(
                                                "Downloaded document is empty");

                        }

                        String detectedType = detector.detect(
                                        resource.content());

                        String checksum = checksumService.sha256(
                                        resource.content());

                        return new DocumentContent(

                                        resource.content(),

                                        resource.fileName(),

                                        detectedType,

                                        resource.size(),

                                        checksum,

                                        BUCKET,

                                        null

                        );

                } catch (Exception e) {

                        throw new IllegalStateException(
                                        "Unable to download document: " + uri,
                                        e);

                }

        }

}