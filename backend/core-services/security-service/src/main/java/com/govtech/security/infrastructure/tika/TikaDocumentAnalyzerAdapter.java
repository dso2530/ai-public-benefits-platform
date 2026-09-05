package com.govtech.security.infrastructure.tika;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;

import com.govtech.security.domain.model.DocumentAnalysis;
import com.govtech.security.domain.port.DocumentAnalyzerPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class TikaDocumentAnalyzerAdapter
        implements DocumentAnalyzerPort {

    private final Tika tika;

    @Override
    public DocumentAnalysis analyze(
            byte[] content) {

        if (content == null || content.length == 0) {

            throw new IllegalArgumentException(
                    "Document content is empty");
        }

        log.info(
                "Starting Tika analysis size={} bytes",
                content.length);

        try {

            String detectedType = detectContentType(content);

            String extractedText = extractText(content);

            String extension = getExtension(detectedType);

            log.info(
                    "Tika result contentType={} extension={} size={}",
                    detectedType,
                    extension,
                    content.length);

            return new DocumentAnalysis(
                    detectedType,
                    extension,
                    content.length,
                    extractedText);

        } catch (Exception e) {

            log.error(
                    "Tika analysis failed",
                    e);

            throw new RuntimeException(
                    "Unable to analyse document",
                    e);
        }

    }

    private String detectContentType(
            byte[] content) {

        try {

            return tika.detect(
                    new ByteArrayInputStream(content));

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to detect content type",
                    e);
        }

    }

    private String extractText(
            byte[] content)
            throws Exception {

        BodyContentHandler handler = new BodyContentHandler(
                100_000);

        Metadata metadata = new Metadata();

        AutoDetectParser parser = new AutoDetectParser();

        try (InputStream stream = new ByteArrayInputStream(content)) {

            parser.parse(
                    stream,
                    handler,
                    metadata,
                    new ParseContext());

        }

        return handler.toString();

    }

    private String getExtension(
            String contentType) {

        return switch (contentType) {

            case "application/pdf" ->
                "pdf";

            case "image/jpeg" ->
                "jpg";

            case "image/png" ->
                "png";

            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                "docx";

            default ->
                "unknown";
        };

    }

}