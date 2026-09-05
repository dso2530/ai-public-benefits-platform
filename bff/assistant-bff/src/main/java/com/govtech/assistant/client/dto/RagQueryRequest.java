package com.govtech.assistant.client.dto;


public record RagQueryRequest(

        String question,

        String territoryCode

) {
}