package com.govtech.extraction.domain.model;

import java.util.Arrays;

public enum DocumentType {

        IDENTITY_CARD(
                        "identity_card",
                        "identity-card",
                        "document.identity-card.extraction.completed",
                        "DocumentIdentityCardExtractionCompleted"),

        PASSPORT(
                        "passport",
                        "passport",
                        "document.passport.extraction.completed",
                        "PassportExtractionCompleted"),

        TAX_NOTICE(
                        "tax_notice",
                        "tax-notice",
                        "document.tax-notice.extraction.completed",
                        "TaxNoticeExtractionCompleted"),

        PAYSLIP(
                        "payslip",
                        "payslip",
                        "document.payslip.extraction.completed",
                        "PayslipExtractionCompleted"),

        RIB(
                        "rib",
                        "rib",
                        "document.rib.extraction.completed",
                        "RibExtractionCompleted"),

        PROOF_OF_ADDRESS(
                        "proof_of_address",
                        "proof-of-address",
                        "document.proof-of-address.extraction.completed",
                        "ProofOfAddressExtractionCompleted"),

        LEASE(
                        "lease",
                        "lease",
                        "document.lease.extraction.completed",
                        "LeaseExtractionCompleted"),

        RENT_RECEIPT(
                        "rent_receipt",
                        "rent-receipt",
                        "document.rent-receipt.extraction.completed",
                        "RentReceiptExtractionCompleted"),

        ENERGY_BILL(
                        "energy_bill",
                        "energy-bill",
                        "document.energy-bill.extraction.completed",
                        "EnergyBillExtractionCompleted"),

        HOME_INSURANCE(
                        "home_insurance",
                        "home-insurance",
                        "document.home-insurance.extraction.completed",
                        "HomeInsuranceExtractionCompleted"),

        HOUSING_TAX(
                        "housing_tax",
                        "housing-tax",
                        "document.housing-tax.extraction.completed",
                        "HousingTaxExtractionCompleted"),

        CAF_CERTIFICATE(
                        "caf_certificate",
                        "caf-certificate",
                        "document.caf-certificate.extraction.completed",
                        "CafCertificateExtractionCompleted"),

        UNKNOWN(
                        "unknown",
                        "unknown",
                        "unknown",
                        "DocumentExtractionCompleted");

        private final String code;
        private final String promptName;
        private final String topic;
        private final String eventType;

        DocumentType(
                        String code,
                        String promptName,
                        String topic,
                        String eventType) {
                this.code = code;
                this.promptName = promptName;
                this.topic = topic;
                this.eventType = eventType;
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

        public String eventType() {
                return eventType;
        }

        public static DocumentType from(String value) {
                return Arrays.stream(values())
                                .filter(type -> type.code.equalsIgnoreCase(value))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Unknown document type: " + value));
        }
}