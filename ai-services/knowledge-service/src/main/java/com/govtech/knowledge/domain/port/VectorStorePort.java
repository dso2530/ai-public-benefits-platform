package com.govtech.knowledge.domain.port;


import com.govtech.knowledge.domain.model.DocumentChunk;
import com.govtech.knowledge.domain.model.EmbeddingVector;


public interface VectorStorePort {


    void save(
            DocumentChunk chunk,
            EmbeddingVector vector);

}