package com.govtech.bff.auth.dto;

public record InternalUser(
        String subject,
        String email,
        String token,
        String name
) {}