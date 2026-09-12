package com.govtech.documentextra.domain.model;

import java.util.Map;

import lombok.Builder;

@Builder
public record ExtractionResult(
                String text,
                String contentType,
                Map<String, String> metadata) {

        public ExtractionResult {
                metadata = metadata == null
                                ? Map.of()
                                : Map.copyOf(metadata);
        }

        public String extractedType() {
                return metadata.get("extractor");
        }
}