package com.govtech.connectors.common.downloader;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.model.DocumentPage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HtmlDownloader {

    private final RestClient connectorRestClient;

    public DocumentPage fetch(URI uri) {

        log.info(
                "Downloading HTML page {}",
                uri);

        String html = connectorRestClient.get()

                .uri(uri)

                .header(
                        "User-Agent",
                        "GovTech-Territorial-Intelligence/1.0")

                .accept(
                        MediaType.TEXT_HTML)

                .retrieve()

                .body(String.class);

        if (html == null || html.isBlank()) {

            throw new IllegalStateException(
                    "Empty HTML response from " + uri);

        }

        Document document = Jsoup.parse(
                html,
                uri.toString());

        List<URI> links = document.select("a[href]")
                .stream()

                .map(link -> link.absUrl("href"))

                .filter(url -> !url.isBlank())

                .map(this::safeUri)

                .filter(Objects::nonNull)

                .toList();

        return new DocumentPage(

                uri,

                document.title(),

                extractContent(document),

                MediaType.TEXT_HTML_VALUE,

                Instant.now(),

                links

        );

    }

    private String extractContent(
            Document document) {

        if (document.body() == null) {

            return "";

        }

        return document.body()
                .text();

    }

    private URI safeUri(String url) {

        try {

            return URI.create(url);

        } catch (IllegalArgumentException e) {

            log.warn(
                    "Invalid URL ignored {}",
                    url);

            return null;
        }

    }

}