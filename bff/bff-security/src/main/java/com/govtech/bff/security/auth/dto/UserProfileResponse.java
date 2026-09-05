package com.govtech.bff.security.auth.dto;

public record UserProfileResponse(
    String id, String franceConnectSub, String firstName, String lastName, String email) {}
