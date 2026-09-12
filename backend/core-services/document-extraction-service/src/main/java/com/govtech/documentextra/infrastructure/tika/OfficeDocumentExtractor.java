package com.govtech.documentextra.infrastructure.tika;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.tika.exception.DocumentExtractionException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OfficeDocumentExtractor
        implements DocumentExtractor {

    private static final Set<String> SUPPORTED_TYPES = Set.of(

            "application/msword",

            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",

            "application/vnd.ms-excel",

            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",

            "application/vnd.ms-powerpoint",

            "application/vnd.openxmlformats-officedocument.presentationml.presentation");

    private final Tika tika;

    @Override
    public boolean supports(String contentType) {

        return SUPPORTED_TYPES.contains(
                contentType.toLowerCase());
    }

    @Override
    public ExtractionResult extract(
            InputStream inputStream) {

        try {

            String text = tika.parseToString(inputStream);

            return new ExtractionResult(
                    text,
                    "text/plain",
                    Map.of(
                            "extractor",
                            "TIKA"));

        } catch (IOException | TikaException e) {

            throw new DocumentExtractionException(
                    "Unable to extract document content with Tika",
                    e);
        }
    }
}