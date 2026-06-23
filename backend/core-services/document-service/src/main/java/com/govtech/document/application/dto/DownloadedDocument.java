package com.govtech.document.application.dto;

public record DownloadedDocument(
        String fileName,
        String contentType,
        byte[] content
) {
}