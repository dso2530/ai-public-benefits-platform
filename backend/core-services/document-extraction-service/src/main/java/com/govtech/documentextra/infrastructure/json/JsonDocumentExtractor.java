package com.govtech.documentextra.infrastructure.json;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.json.exception.JsonDocumentExtractorException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JsonDocumentExtractor
        implements DocumentExtractor {

    @Override
    public boolean supports(String contentType) {

        return "application/json".equalsIgnoreCase(contentType)
                || "application/ld+json".equalsIgnoreCase(contentType);
    }

    @Override
    public ExtractionResult extract(
            InputStream inputStream) {

        try {

            String json = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8);

            return new ExtractionResult(
                    json,
                    "application/json",
                    Map.of(
                            "extractor",
                            "JSON"));

        } catch (IOException e) {

            log.error(
                    "JSON extraction failed",
                    e);

            throw new JsonDocumentExtractorException(
                    "JSON extraction failed",
                    e);
        }
    }
}