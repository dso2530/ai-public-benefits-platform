package com.govtech.extraction.infrastructure.llm;

import com.govtech.extraction.application.ExtractionProvider;
import com.govtech.extraction.infrastructure.llm.client.OllamaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OllamaExtractionProvider implements ExtractionProvider {

    private final OllamaClient client;

    @Value("${llm.model}")
    private String model;

    @Override
    public String extract(String prompt) {
        return client.extract(prompt);
    }

    @Override
    public String getModel() {
        return model;
    }
}