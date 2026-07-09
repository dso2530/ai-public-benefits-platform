package com.govtech.profile.domain.exception;

public class CitizenNotFoundException extends RuntimeException {

    public CitizenNotFoundException(String subject) {
        super("Citizen not found with subject: " + subject);
    }
}