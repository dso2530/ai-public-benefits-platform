
package com.govtech.application.service.cerfa.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum CerfaFieldKey {

        NOM("page1_texte_nom_famille"),
        NOM_USAGE("page1_texte_nom_d_usage"),
        PRENOM("page1_texte_prenom"),
        DATE_NAISSANCE("page1_texte_date_naissance"),
        NUMERO_SECURITE_SOCIALE("page1_texte_numero_securite_sociale"),
        ADRESSE("page1_texte_adresse_logement_lequel_demandez"),
        COMPLEMENT("page1_texte_complement"),
        CODE_POSTAL("page1_texte_code_postal"),
        TELEPHONE("page1_texte_numeros_telephone_domicile"),
        EMAIL("page1_texte_adresse_mel"),

        // Cases à cocher page 1
        LOGEMENT_ADRESSE_PRINCIPALE_OUI("page1_oui_logement_adresse_principale"),
        LOGEMENT_ADRESSE_PRINCIPALE_NON("page1_non_logement_adresse_principale_oui"),

        SOCIAUX_OUI("page1_oui_sociaux"),
        SOCIAUX_NON("page1_non_sociaux_oui"),

        SOCIAUX_OUI_NON_SOCIAUX_OUI("page1_oui_sociaux_oui_non_sociaux"),
        SOCIAUX_OUI_NON_SOCIAUX_NON("page1_non_sociaux_oui_non_sociaux"),

        NOM_ADRESSE_ORGANISME("page1_texte_nom_adresse_l_organisme"),
        DATE_ATTRIBUTION_BOURSE("page1_texte_date_d_attribution_bourse");

        private final String value;

        CerfaFieldKey(String value) {
                this.value = value;
        }

        @JsonCreator
        public static CerfaFieldKey fromValue(String value) {
                for (CerfaFieldKey key : values()) {
                        if (key.value.equals(value)) {
                                return key;
                        }
                }

                return null;
        }

        public String getValue() {
                return value;
        }
}