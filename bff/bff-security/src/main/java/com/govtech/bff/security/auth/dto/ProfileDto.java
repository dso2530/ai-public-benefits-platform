package com.govtech.bff.security.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfileDto(
        @NotBlank String subject,
        @NotBlank String email,
        String firstName,
        String lastName,
        @NotBlank String city,
        String postalCode,
        @NotBlank String housingStatus,
        Integer childrenCount,
        Boolean singleParent) {
}
