package com.govtech.bff.security.auth.model;

import lombok.Builder;

@Builder
public record InternalAuthentication(
        String accessToken,
        long expiresIn) {
}