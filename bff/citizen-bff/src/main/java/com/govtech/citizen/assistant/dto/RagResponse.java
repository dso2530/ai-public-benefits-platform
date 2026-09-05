package com.govtech.citizen.assistant.dto;

import java.util.List;
import java.util.Map;

public record RagResponse(

                String answer,

                List<RagSource> sources,

                Long processingTimeMs) {

        public record RagSource(

                        Long documentId,

                        Integer chunkNumber,

                        Double score,

                        String content,

                        Map<String, String> metadata

        ) {
        }
}