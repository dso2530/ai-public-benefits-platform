package com.govtech.rag.domain.model;


public record Query(

        String question,

        String territoryCode,

        String source

) {}

