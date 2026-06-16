package com.govtech.bff.auth.dto;
public record UserProfileResponse(
        String id,
        String franceConnectSub,
        String firstName,
        String lastName,
        String email
) {
}