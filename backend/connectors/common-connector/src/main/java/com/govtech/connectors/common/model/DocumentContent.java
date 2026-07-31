package com.govtech.connectors.common.model;

public record DocumentContent(

        byte[] content,

        String fileName,

        String contentType,

        long size,

        String checksum

) {

    public DocumentContent {

        if (content == null) {

            throw new IllegalArgumentException(
                    "Document content cannot be null");

        }

        if (fileName == null || fileName.isBlank()) {

            fileName = "unknown";

        }

        if (contentType == null || contentType.isBlank()) {

            contentType = "application/octet-stream";

        }

        if (checksum == null || checksum.isBlank()) {

            throw new IllegalArgumentException(
                    "Document checksum cannot be null");

        }

    }

}