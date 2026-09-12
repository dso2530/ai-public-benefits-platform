
package com.govtech.citizen.auth.dto;

public record KeycloakIdentity(
        String subject,
        String email,
        String fullName) {
}