package com.govtech.documentextra.infrastructure.ocr.dto;

public record OCRTextBlock(
        String text,
        Double confidence
) {
}