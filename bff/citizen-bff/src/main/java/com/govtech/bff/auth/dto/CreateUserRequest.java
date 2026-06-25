package com.govtech.bff.auth.dto;

public record CreateUserRequest(
    String franceConnectSub, String firstName, String lastName, String email) {}
