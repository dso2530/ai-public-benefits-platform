package com.govtech.documentextra.infrastructure.ocr;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.govtech.documentextra.domain.model.ExtractionResult;
import com.govtech.documentextra.domain.port.DocumentExtractor;
import com.govtech.documentextra.infrastructure.ocr.exception.OCRException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaddleOCRDocumentExtractor
        implements DocumentExtractor {

    private final PaddleOCRProvider provider;

    @Override
    public boolean supports(String contentType) {

        return "application/pdf".equalsIgnoreCase(contentType)
                || contentType.toLowerCase().startsWith("image/");
    }

    @Override
    public ExtractionResult extract(
            InputStream inputStream) {

        try {

            String text = provider.extractText(inputStream);

            return new ExtractionResult(
                    text,
                    "text/plain",
                    Map.of(
                            "extractor",
                            "PADDLE_OCR"));

        } catch (IOException e) {

            throw new OCRException(
                    "OCR extraction failed",
                    e);
        }
    }
}