package com.govtech.knowledge.infrastructure.embedding;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import com.govtech.knowledge.domain.model.EmbeddingVector;
import com.govtech.knowledge.domain.port.EmbeddingProviderPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OllamaEmbeddingAdapter
                implements EmbeddingProviderPort {

        private final EmbeddingModel embeddingModel;

        @Override
        public EmbeddingVector generate(
                        String text) {

                float[] vector = embeddingModel.embed(text);

                return new EmbeddingVector(
                                vector);

        }
}