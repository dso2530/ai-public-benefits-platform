package com.govtech.documentextra.infrastructure.text;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.text.exception.TextExtractionException;

@Component
public class TextDocumentExtractor
        implements DocumentExtractor {

    @Override
    public boolean supports(String contentType) {

        return "text/plain".equalsIgnoreCase(contentType);
    }

    @Override
    public ExtractionResult extract(
            InputStream inputStream) {

        try {

            String text = new String(
                    inputStream.readAllBytes(),
                    StandardCharsets.UTF_8)
                    .trim();

            return new ExtractionResult(
                    text,
                    "text/plain",
                    Map.of(
                            "extractor",
                            "TEXT"));

        } catch (Exception e) {

            throw new TextExtractionException(
                    "Text extraction failed",
                    e);
        }
    }
}