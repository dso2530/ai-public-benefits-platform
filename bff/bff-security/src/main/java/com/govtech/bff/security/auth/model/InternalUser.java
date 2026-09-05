package com.govtech.bff.security.auth.model;

public record InternalUser(String subject, String email, String token, String name) {}
