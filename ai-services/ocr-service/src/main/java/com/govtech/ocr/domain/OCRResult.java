package com.govtech.ocr.domain;

import java.time.Instant;

public record OCRResult(
        Long documentId,
        String text,
        String language,
        double confidence,
        int pageCount,
        Instant processedAt
) {}