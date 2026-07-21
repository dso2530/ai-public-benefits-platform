package com.govtech.application.service.dto;

public record PackageDocument(
        String fileName,
        byte[] content) {
}