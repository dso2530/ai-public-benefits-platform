package com.govtech.connectors.infrastructure.datagouv;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ResourceExtractor {

    public List<DataGouvResource> extract(
            DataGouvDatasetResponse.Dataset dataset) {

        if (dataset.resources() == null) {

            return List.of();

        }

        return dataset.resources()

                .stream()

                .filter(resource -> resource.url() != null)

                .filter(this::isSupportedFormat)

                .map(this::map)

                .toList();

    }

    private DataGouvResource map(
            DataGouvDatasetResponse.Dataset.Resource resource) {

        URI uri;

        try {

            uri = new URI(resource.url());

        } catch (URISyntaxException e) {

            log.warn(
                    "Invalid DataGouv resource url {}",
                    resource.url());

            return null;

        }

        return new DataGouvResource(

                resource.id(),

                resource.title(),

                uri,

                normalizeFormat(
                        resource.format()),

                resource.filesize() == null
                        ? 0L
                        : resource.filesize(),

                resource.mime()

        );

    }

    private boolean isSupportedFormat(
            DataGouvDatasetResponse.Dataset.Resource resource) {

        if (resource.format() == null) {

            return false;

        }

        String format = normalizeFormat(resource.format());

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