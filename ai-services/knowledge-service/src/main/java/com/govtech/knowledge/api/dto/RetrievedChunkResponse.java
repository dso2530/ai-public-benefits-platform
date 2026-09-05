package com.govtech.knowledge.api.dto;

import java.util.Map;

public record RetrievedChunkResponse(

        Long documentId,

        Integer chunkNumber,

        String content,

        Double score,

        Map<String, String> metadata

) {
}