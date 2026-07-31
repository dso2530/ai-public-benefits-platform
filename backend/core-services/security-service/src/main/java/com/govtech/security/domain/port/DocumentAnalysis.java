package com.govtech.security.domain.port;

public record DocumentAnalysis(

        String detectedContentType,

        String fileExtension,

        long size,

        String extractedText

) {
}