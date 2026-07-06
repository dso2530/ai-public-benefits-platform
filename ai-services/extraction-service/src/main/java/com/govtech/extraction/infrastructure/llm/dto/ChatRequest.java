package com.govtech.extraction.infrastructure.llm.dto;

import java.util.List;

public record ChatRequest(
        String model,
        List<Message> messages,
        boolean stream, boolean think) {
}