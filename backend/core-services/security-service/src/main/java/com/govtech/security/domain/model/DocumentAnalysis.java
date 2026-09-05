package com.govtech.security.domain.model;

public record DocumentAnalysis(

        String detectedContentType,

        String fileExtension,

        long size,

        String extractedText

) {
}