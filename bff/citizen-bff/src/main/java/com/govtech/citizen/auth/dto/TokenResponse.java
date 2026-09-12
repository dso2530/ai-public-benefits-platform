package com.govtech.citizen.auth.dto;

public record TokenResponse(
                String accessToken,

                String tokenType,

                long expiresIn

) {
}
