package com.govtech.rag.infrastructure.knowledge.dto;

import java.util.List;

public record SearchResponse(

                List<RetrievedChunkResponse> chunks,

                int count

) {
}