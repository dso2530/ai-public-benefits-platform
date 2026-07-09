
package com.govtech.mock;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.govtech.events.DocumentExtractionCompletedEvent;
import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.events.DocumentUploadedEvent;
import com.govtech.platform.messaging.publisher.EventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentUploadedPublisher implements CommandLineRunner {

        private final EventPublisher eventPublisher;

        @Override
        public void run(String... args) {

                DocumentOCRCompletedEvent event = DocumentOCRCompletedEvent.newBuilder()
                                .setDocumentId(12345L)
                                .setSubject("f46b0253-b104-44a5-af58-f4a5bd306d63")
                                .setBucket("documents")
                                .setObjectKey("tax-notice/2025/avis-impot.pdf")
                                .setDocumentType("TAX_NOTICE")
                                .setContentType("application/pdf")
                                .setText("""
                                                DIRECTION GÉNÉRALE DES FINANCES PUBLIQUES

                                                AVIS D'IMPÔT SUR LES REVENUS 2024

                                                Nom : FOFANA
                                                Prénom : MAMADOU

                                                Contribuable 2 :
                                                Nom : DIALLO
                                                Prénom : FATOUMATA BINTA

                                                Adresse :
                                                89 RUE DE GUISNES
                                                59200 TOURCOING

                                                Centre des Finances Publiques SIP TOURCOING
                                                PLACE DE LA RÉSISTANCE
                                                59209 TOURCOING CEDEX

                                                Année des revenus : 2024
                                                Année d'imposition : 2025

                                                Revenu fiscal de référence : 35 147

                                                Nombre de parts : 4,00

                                                Impôt sur le revenu : 356 €

                                                Montant à rembourser : 356,00 €

                                                Date de remboursement : 25/07/2025

                                                Date de mise en recouvrement : 31/07/2025

                                                Référence : 25 59 A513124 77

                                                IBAN : FR76 3000 4000 4200 0000 0002 827
                                                BIC : BNPAFRPPXXX
                                                """)
                                .setProcessedAt(Instant.now().toString())
                                .build();

                eventPublisher.publish(
                                "document.ocr.completed",
                                event.getObjectKey(),
                                event);
        }

}