package com.govtech.extraction.infrastructure.llm.dto;

public record Message(
        String role,
        String content) {
}