package com.govtech.knowledge.domain.model;

import java.util.Map;

public record RetrievedChunk(

        Long documentId,

        Integer chunkNumber,

        String content,

        Double score,

        Map<String, String> metadata

) {
}