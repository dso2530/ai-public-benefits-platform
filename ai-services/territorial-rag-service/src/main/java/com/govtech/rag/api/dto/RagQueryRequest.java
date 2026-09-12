package com.govtech.rag.api.dto;

public record RagQueryRequest(

        String question,

        String territoryCode,

        String source

) {}