package com.govtech.rag.infrastructure.knowledge.dto;

import java.util.Map;

public record RetrievedChunkResponse(

        Long documentId,

        Integer chunkNumber,

        String content,

        Double score,

        Map<String,String> metadata

) {}