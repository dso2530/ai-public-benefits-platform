package com.govtech.assistant.api.dto;

import java.util.List;

public record AssistantResponse(

        String answer,

        List<AssistantSourceDto> sources

) {

}