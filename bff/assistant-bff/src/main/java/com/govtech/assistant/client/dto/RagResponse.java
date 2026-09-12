package com.govtech.assistant.client.dto;

import java.util.List;
import java.util.Map;

public record RagResponse(

        String answer,

        List<RagSource> sources,

        long processingTimeMs

) {

    public record RagSource(

            Long documentId,

            Integer chunkNumber,

            Double score,

            String content,

            Map<String, String> metadata

    ) {
    }
}