package com.govtech.extraction.application.prompt;

public interface PromptProvider {

    String buildPrompt(String documentType, String text);

}