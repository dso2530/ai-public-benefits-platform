package com.govtech.rag.infrastructure.ollama;

import java.time.Duration;
import java.time.Instant;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import com.govtech.rag.domain.port.LlmPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaChatAdapter implements LlmPort {

        private final ChatClient chatClient;

        @Override
        public String generate(String prompt) {

                log.info(
                                "Calling Ollama LLM (promptSize={} chars)",
                                prompt.length());

                Instant start = Instant.now();

                String response = chatClient
                                .prompt()
                                .user(prompt)
                                .call()
                                .content();

                long duration = Duration.between(
                                start,
                                Instant.now())
                                .toMillis();

                log.info(
                                "Ollama response received in {} ms (responseSize={} chars)",
                                duration,
                                response != null ? response.length() : 0);

                return response;
        }

        @Override
        public Flux<String> generateStream(String prompt) {

                return chatClient
                                .prompt()
                                .user(prompt)
                                .stream()
                                .content();
        }
}