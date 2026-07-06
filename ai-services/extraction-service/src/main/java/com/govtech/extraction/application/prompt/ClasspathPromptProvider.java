package com.govtech.extraction.application.prompt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ClasspathPromptProvider implements PromptProvider {

    @Override
    public String buildPrompt(String documentType, String text) {

        String template = loadTemplate(documentType);

        return template.formatted(text);
    }

    private String loadTemplate(String documentType) {

        String filename = documentType.toLowerCase() + ".txt";

        try {

            var resource = new ClassPathResource("prompts/" + filename);

            if (!resource.exists()) {
                log.warn("Prompt '{}' not found. Falling back to default prompt.", filename);
                resource = new ClassPathResource("prompts/default.txt");
            }

            return new String(
                    resource.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new RuntimeException("Unable to load prompt " + filename, e);
        }
    }
}