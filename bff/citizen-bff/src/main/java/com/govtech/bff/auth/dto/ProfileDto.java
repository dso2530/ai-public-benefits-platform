package com.govtech.bff.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ProfileDto(
    String subject,
    String email,
    String firstName,
    String lastName,
    String city,
    String postalCode,
    @NotBlank String housingStatus,
    Integer childrenCount,
    Boolean singleParent) {}
