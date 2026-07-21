package com.govtech.extraction.domain.model;

import java.util.Arrays;

public enum DocumentType {

        IDENTITY_CARD(
                        "identity_card",
                        "identity-card",
                        "document.identity-card.extraction.completed"),

        PASSPORT(
                        "passport",
                        "passport",
                        "document.passport.extraction.completed"),

        TAX_NOTICE(
                        "tax_notice",
                        "tax-notice",
                        "document.tax-notice.extraction.completed"),

        PAYSLIP(
                        "payslip",
                        "payslip",
                        "document.payslip.extraction.completed"),

        RIB(
                        "rib",
                        "rib",
                        "document.rib.extraction.completed"),

        PROOF_OF_ADDRESS(
                        "proof_of_address",
                        "proof-of-address",
                        "document.proof-of-address.extraction.completed"),

        LEASE(
                        "lease",
                        "lease",
                        "document.lease.extraction.completed"),

        RENT_RECEIPT(
                        "rent_receipt",
                        "rent-receipt",
                        "document.rent-receipt.extraction.completed"),

        ENERGY_BILL(
                        "energy_bill",
                        "energy-bill",
                        "document.energy-bill.extraction.completed"),

        HOME_INSURANCE(
                        "home_insurance",
                        "home-insurance",
                        "document.home-insurance.extraction.completed"),

        HOUSING_TAX(
                        "housing_tax",
                        "housing-tax",
                        "document.housing-tax.extraction.completed"),

        CAF_CERTIFICATE(
                        "caf_certificate",
                        "caf-certificate",
                        "document.caf-certificate.extraction.completed");

        private final String code;
        private final String promptName;
        private final String topic;

        DocumentType(
                        String code,
                        String promptName,
                        String topic) {
                this.code = code;
                this.promptName = promptName;
                this.topic = topic;
        }

        public String code() {
                return code;
        }

        public String promptName() {
                return promptName;
        }

        public String topic() {
                return topic;
        }

        public static DocumentType from(String value) {

                return Arrays.stream(values())
                                .filter(type -> type.code.equalsIgnoreCase(value))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Unknown document type: " + value));
        }
}