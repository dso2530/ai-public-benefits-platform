package com.govtech.citizen.assistant.dto;

import java.util.List;

public record AssistantResponse(

        String answer,

        List<AssistantSourceDto> sources

) {

}