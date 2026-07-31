package com.govtech.connectors.common.downloader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.model.DownloadedResource;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HttpDownloader implements Downloader {

        private final RestClient connectorRestClient;

        @Override
        public DownloadedResource download(
                        URI uri) throws IOException {

                var response = connectorRestClient.get()

                                .uri(uri)

                                .retrieve()

                                .toEntity(
                                                byte[].class);

                byte[] body = response.getBody();

                if (body == null) {

                        throw new IOException(
                                        "Empty response from " + uri);

                }

                String contentType = response.getHeaders()
                                .getContentType() != null

                                                ? response.getHeaders()
                                                                .getContentType()
                                                                .toString()

                                                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

                return new DownloadedResource(

                                extractFileName(uri),

                                contentType,

                                body.length,

                                new ByteArrayInputStream(body).readAllBytes()

                );

        }

        private String extractFileName(
                        URI uri) {

                String path = uri.getPath();

                if (path == null || path.isBlank()) {

                        return "unknown";

                }

                int index = path.lastIndexOf('/');

                return index >= 0
                                ? path.substring(index + 1)
                                : path;

        }

}