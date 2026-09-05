package com.govtech.platform.messaging.event;

import java.util.Map;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import com.govtech.events.application.ApplicationGeneratedEvent;
import com.govtech.events.bank.RibExtractionCompletedEvent;
import com.govtech.events.caf.CafCertificateExtractionCompletedEvent;
import com.govtech.events.document.DocumentExtractionCompletedEvent;
import com.govtech.events.document.DocumentIngestedEvent;
import com.govtech.events.document.DocumentUploadedEvent;
import com.govtech.events.eligibility.EligibilityCheckedEvent;
import com.govtech.events.energy.EnergyBillExtractionCompletedEvent;
import com.govtech.events.homeinsurance.HomeInsuranceExtractionCompletedEvent;
import com.govtech.events.housingtax.HousingTaxExtractionCompletedEvent;
import com.govtech.events.identity.IdentityCardExtractionCompletedEvent;
import com.govtech.events.lease.LeaseExtractionCompletedEvent;
import com.govtech.events.passport.PassportExtractionCompletedEvent;
import com.govtech.events.payslip.PayslipExtractionCompletedEvent;
import com.govtech.events.profile.ProfileUpdatedEvent;
import com.govtech.events.proofofaddress.ProofOfAddressExtractionCompletedEvent;
import com.govtech.events.rentreceipt.RentReceiptExtractionCompletedEvent;
import com.govtech.events.security.DocumentScanCompletedEvent;
import com.govtech.events.security.DocumentScanRequestedEvent;
import com.govtech.events.tax.TaxNoticeExtractionCompletedEvent;

@Component
public class AvroEventTypeRegistry {

        private final Map<String, Class<? extends SpecificRecord>> types = Map.ofEntries(

                        // Security
                        Map.entry(
                                        "DocumentScanRequested",
                                        DocumentScanRequestedEvent.class),

                        Map.entry(
                                        "DocumentScanCompleted",
                                        DocumentScanCompletedEvent.class),

                        // Document lifecycle
                        Map.entry(
                                        "DocumentUploaded",
                                        DocumentUploadedEvent.class),

                        Map.entry(
                                        "DocumentIngested",
                                        DocumentIngestedEvent.class),

                        // Generic extraction
                        Map.entry(
                                        "DocumentExtractionCompleted",
                                        DocumentExtractionCompletedEvent.class),

                        // Document extraction
                        Map.entry(
                                        "IdentityCardExtractionCompleted",
                                        IdentityCardExtractionCompletedEvent.class),

                        Map.entry(
                                        "PassportExtractionCompleted",
                                        PassportExtractionCompletedEvent.class),

                        Map.entry(
                                        "TaxNoticeExtractionCompleted",
                                        TaxNoticeExtractionCompletedEvent.class),

                        Map.entry(
                                        "PayslipExtractionCompleted",
                                        PayslipExtractionCompletedEvent.class),

                        Map.entry(
                                        "RibExtractionCompleted",
                                        RibExtractionCompletedEvent.class),

                        Map.entry(
                                        "ProofOfAddressExtractionCompleted",
                                        ProofOfAddressExtractionCompletedEvent.class),

                        Map.entry(
                                        "LeaseExtractionCompleted",
                                        LeaseExtractionCompletedEvent.class),

                        Map.entry(
                                        "RentReceiptExtractionCompleted",
                                        RentReceiptExtractionCompletedEvent.class),

                        Map.entry(
                                        "EnergyBillExtractionCompleted",
                                        EnergyBillExtractionCompletedEvent.class),

                        Map.entry(
                                        "HomeInsuranceExtractionCompleted",
                                        HomeInsuranceExtractionCompletedEvent.class),

                        Map.entry(
                                        "HousingTaxExtractionCompleted",
                                        HousingTaxExtractionCompletedEvent.class),

                        Map.entry(
                                        "CafCertificateExtractionCompleted",
                                        CafCertificateExtractionCompletedEvent.class),
                        Map.entry(
                                        "ProfileUpdated",
                                        ProfileUpdatedEvent.class),
                        Map.entry(
                                        "EligibilityChecked",
                                        EligibilityCheckedEvent.class),
                        Map.entry(
                                        "ApplicationGenerated",
                                        ApplicationGeneratedEvent.class));

        public Class<? extends SpecificRecord> resolve(String eventType) {

                Class<? extends SpecificRecord> type = types.get(eventType);

                if (type == null) {
                        throw new IllegalArgumentException(
                                        "Unknown event type: " + eventType);
                }

                return type;
        }
}