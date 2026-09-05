package com.govtech.extraction.domain.model;

public enum DocumentOrigin {

    /**
     * Document envoyé par un utilisateur
     * (justificatifs, CERFA, pièces dossier)
     */
    USER_UPLOAD,

    /**
     * Document découvert par un connector externe
     * (data.gouv, service public, etc.)
     */
    CONNECTOR,

    /**
     * Document généré par la plateforme
     */
    SYSTEM_GENERATED

}