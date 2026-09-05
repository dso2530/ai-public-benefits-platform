package com.govtech.rag.infrastructure.ollama;

import java.time.Duration;
import java.time.Instant;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import com.govtech.rag.domain.port.EmbeddingPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OllamaEmbeddingAdapter
        implements EmbeddingPort {

    private final EmbeddingModel embeddingModel;

    @Override
    public float[] embed(
            String text) {

        log.info(
                "Generating embedding (textSize={} chars)",
                text.length());

        if (log.isDebugEnabled()) {

            log.debug(
                    "Embedding text:\n{}",
                    text.length() > 1000
                            ? text.substring(0, 1000) + "\n...[truncated]"
                            : text);
        }

        Instant start = Instant.now();

        try {

            float[] embedding =
                    embeddingModel.embed(text);

            long duration =
                    Duration.between(
                            start,
                            Instant.now())
                            .toMillis();

            log.info(
                    "Embedding generated in {} ms (dimension={})",
                    duration,
                    embedding.length);

            return embedding;

        } catch (Exception e) {

            log.error(
                    "Unable to generate embedding",
                    e);

            throw e;
        }
    }
}