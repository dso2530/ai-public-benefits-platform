package com.govtech.knowledge.domain.port;


import com.govtech.knowledge.domain.model.EmbeddingVector;


public interface EmbeddingProviderPort {


    EmbeddingVector generate(
            String text);

}