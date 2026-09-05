package com.govtech.rag.api.dto;

import java.util.List;

public record RagAnswerResponse(

        String answer,

        List<RagSourceResponse> sources

) {}