package com.govtech.rag.infrastructure.knowledge.dto;


public record SearchRequest(

         float[] embedding,

        String territoryCode,

        int limit

) {}