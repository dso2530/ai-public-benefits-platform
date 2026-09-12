package com.govtech.citizen.auth.dto;

public record MobileTokenRequest(
        String code,
        String state) {
}