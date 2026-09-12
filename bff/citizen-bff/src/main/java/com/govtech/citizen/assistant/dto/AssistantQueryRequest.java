package com.govtech.citizen.assistant.dto;

import jakarta.validation.constraints.NotBlank;

public record AssistantQueryRequest(

                @NotBlank String question,

                String territoryCode

) {

}