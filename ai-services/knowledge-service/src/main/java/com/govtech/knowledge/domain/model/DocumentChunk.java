package com.govtech.knowledge.domain.model;

import java.util.Map;

public record DocumentChunk(

        Long documentId,

        Integer chunkNumber,

        String content,

        Map<String, String> metadata

) {

}
