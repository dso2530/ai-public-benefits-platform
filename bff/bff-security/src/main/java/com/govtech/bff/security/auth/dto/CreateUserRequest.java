package com.govtech.bff.security.auth.dto;

public record CreateUserRequest(
    String franceConnectSub, String firstName, String lastName, String email) {}
