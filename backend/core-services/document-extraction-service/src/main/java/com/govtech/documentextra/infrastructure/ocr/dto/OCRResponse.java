package com.govtech.documentextra.infrastructure.ocr.dto;


public record OCRResponse(
    String filename,
    String text
) {}