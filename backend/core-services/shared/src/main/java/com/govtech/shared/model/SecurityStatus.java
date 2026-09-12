package com.govtech.shared.model;

public enum SecurityStatus {

    /**
     * Document reçu mais pas encore analysé
     */
    PENDING,
    /**
     * Analyse antivirus et sécurité en cours
     */
    SCANNING,
    /**
     * Document validé par le security-service
     */
    CLEAN,
    /**
     * Document infecté détecté
     */
    INFECTED,
    /**
     * Document non conforme aux règles de sécurité
     * (format interdit, archive invalide...)
     */
    REJECTED
}