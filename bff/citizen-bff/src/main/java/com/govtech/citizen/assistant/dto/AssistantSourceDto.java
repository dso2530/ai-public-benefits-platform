package com.govtech.citizen.assistant.dto;

public record AssistantSourceDto(

        Long documentId,

        Integer chunkNumber,

        Double score,

        String content

) {

}