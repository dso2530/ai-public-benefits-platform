package com.govtech.knowledge.api.dto;

public record SearchRequest(

        float[] embedding,

        String territoryCode,

        int limit

) {
}