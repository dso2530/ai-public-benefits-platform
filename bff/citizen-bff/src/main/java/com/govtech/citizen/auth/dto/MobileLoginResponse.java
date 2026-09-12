package com.govtech.citizen.auth.dto;

public record MobileLoginResponse(
        String authorizationUrl,
        String state) {
}