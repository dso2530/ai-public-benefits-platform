package com.govtech.documentextra.infrastructure.ocr.dto;

import java.util.List;

public record OCRResult(
        List<OCRTextBlock> blocks
) {
}