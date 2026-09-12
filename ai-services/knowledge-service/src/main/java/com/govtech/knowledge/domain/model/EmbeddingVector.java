package com.govtech.knowledge.domain.model;

public record EmbeddingVector(

        float[] values

) {

    public int dimensions() {

        return values.length;

    }

}