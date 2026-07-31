package com.govtech.consent.api.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConsentRequest(

        @NotNull UUID userId,

        @NotBlank String purpose,

        @NotBlank String version,

        @NotBlank String source

) {
}