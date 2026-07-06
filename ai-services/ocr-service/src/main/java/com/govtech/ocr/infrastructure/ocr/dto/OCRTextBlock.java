package com.govtech.ocr.infrastructure.ocr.dto;

public record OCRTextBlock(
        String text,
        Double confidence
) {
}