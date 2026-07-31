package com.govtech.connectors.common.spi;

import com.govtech.connectors.common.security.AuthenticationType;

public interface Connector {

    /**
     * Nom unique du connector.
     */
    String name();

    /**
     * Indique si le connector est activé.
     */
    default boolean enabled() {
        return true;
    }

    /**
     * Type d'authentification utilisé.
     */
    default AuthenticationType authentication() {
        return AuthenticationType.NONE;
    }

    /**
     * Vérification de disponibilité.
     */
    default ConnectorHealth health() {
        return ConnectorHealth.UP;
    }

}