package com.govtech.knowledge.api.dto;

import java.util.List;

public record SearchResponse(

        List<RetrievedChunkResponse> chunks,

        int count) {
}