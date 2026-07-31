package com.govtech.connectors.common.model;

import java.util.Map;

public record DocumentMetadata(

        String title,

        String description,

        String documentType,

        Map<String, String> attributes

) {

    public DocumentMetadata {

        if (attributes == null) {
            attributes = Map.of();
        }

    }

    public static DocumentMetadata empty() {

        return new DocumentMetadata(
                null,
                null,
                null,
                Map.of());

    }

}