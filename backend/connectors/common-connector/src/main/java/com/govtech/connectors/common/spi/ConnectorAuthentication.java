package com.govtech.connectors.common.spi;

import com.govtech.connectors.common.security.AuthenticationType;

public interface ConnectorAuthentication {

    /**
     * Type d'authentification utilisé.
     */
    AuthenticationType type();

    /**
     * Indique si l'authentification est configurée.
     */
    default boolean available() {

        return true;

    }

}