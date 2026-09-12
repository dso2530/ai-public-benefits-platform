package com.govtech.assistant.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AssistantQueryRequest(

        @NotBlank String question,

        String territoryCode

) {

}