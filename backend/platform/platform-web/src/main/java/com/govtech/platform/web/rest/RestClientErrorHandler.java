package com.govtech.platform.web.rest;

import com.govtech.platform.web.error.ErrorCode;
import com.govtech.platform.web.error.ApplicationException;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;

public final class RestClientErrorHandler {

    private RestClientErrorHandler() {
    }

    public static ApplicationException handle(
            RestClientResponseException ex,
            String serviceName) {

        HttpStatusCode status = ex.getStatusCode();

        if (status.value() == 404) {
            return new ApplicationException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "Ressource introuvable dans le service " + serviceName);
        }

        if (status.is4xxClientError()) {
            return new ApplicationException(
                    ErrorCode.EXTERNAL_SERVICE_ERROR,
                    "Le service " + serviceName
                            + " a rejeté la requête.");
        }

        if (status.is5xxServerError()) {
            return new ApplicationException(
                    ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE,
                    "Le service " + serviceName
                            + " est temporairement indisponible.");
        }

        return new ApplicationException(
                ErrorCode.EXTERNAL_SERVICE_ERROR,
                "Erreur HTTP du service " + serviceName);
    }
}
