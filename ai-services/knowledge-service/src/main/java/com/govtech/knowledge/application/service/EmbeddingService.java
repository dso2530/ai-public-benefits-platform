package com.govtech.knowledge.application.service;

import org.springframework.stereotype.Service;

import com.govtech.knowledge.domain.model.EmbeddingVector;
import com.govtech.knowledge.domain.port.EmbeddingProviderPort;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class EmbeddingService {


    private final EmbeddingProviderPort embeddingProvider;



    public EmbeddingVector generate(
            String text) {


        if (text == null || text.isBlank()) {

            throw new IllegalArgumentException(
                    "Text cannot be empty");

        }


        return embeddingProvider.generate(
                text);

    }

}