package com.govtech.rag.api.dto;

public record RagSourceResponse(

                Long documentId,

                Integer chunkNumber,

                Double score

) {
}