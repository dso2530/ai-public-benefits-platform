package com.govtech.assistant.api.dto;

public record AssistantSourceDto(

        Long documentId,

        Integer chunkNumber,

        Double score,

        String content

) {

}