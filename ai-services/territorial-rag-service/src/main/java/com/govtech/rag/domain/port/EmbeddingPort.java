package com.govtech.rag.domain.port;


public interface EmbeddingPort {


    float[] embed(
            String text);

}