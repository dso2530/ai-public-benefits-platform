package com.govtech.connectors.infrastructure.caf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component("cafResourceExtractor")
@Slf4j
public class ResourceExtractor {

    public List<CafDocumentResource> extract(
            CafDocumentSearchResponse.Document document) {

        if (document == null ||
                document.url() == null) {

            return List.of();
        }

        if (!isSupportedFormat(document)) {
            return List.of();
        }

        CafDocumentResource resource = map(document);

        if (resource == null) {
            return List.of();
        }

        return List.of(resource);
    }

    private CafDocumentResource map(
            CafDocumentSearchResponse.Document document) {

        URI uri;

        try {

            uri = new URI(document.url());

        } catch (URISyntaxException e) {

            log.warn(
                    "Invalid CAF resource url {}",
                    document.url());

            return null;
        }

        return new CafDocumentResource(

                document.id(),

                document.title(),

                uri,

                normalizeFormat(
                        document.format()),

                document.filesize() == null
                        ? 0L
                        : document.filesize(),

                document.mime()

        );
    }

    private boolean isSupportedFormat(
            CafDocumentSearchResponse.Document document) {

        if (document.format() == null) {
            return false;
        }

        String format = normalizeFormat(document.format());

        return switch (format) {

            case "pdf",
                    "csv",
                    "xlsx",
                    "xls",
                    "json",
                    "ods" ->
                true;

            default -> false;
        };
    }

    private String normalizeFormat(
            String format) {

        return format
                .toLowerCase()
                .trim()
                .split("/")[0];
    }
}