package com.govtech.ocr.infrastructure.ocr.dto;


public record OCRResponse(
    String filename,
    String text
) {}