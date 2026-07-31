package com.govtech.security.api.dto;

public record ScanDocumentRequest(

        byte[] content,

        String sha256

) {
}