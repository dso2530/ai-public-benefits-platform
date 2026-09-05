package com.govtech.apply.application.dto;

public record PackageDocument(
        String fileName,
        byte[] content) {
}