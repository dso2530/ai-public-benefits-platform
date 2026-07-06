
package com.govtech.mock;

import java.time.Instant;

import org.springframework.boot.CommandLineRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.govtech.events.DocumentOCRCompletedEvent;
import com.govtech.events.DocumentUploadedEvent;
import com.govtech.platform.messaging.publisher.KafkaEventPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DocumentUploadedPublisher implements CommandLineRunner {

        private final KafkaEventPublisher kafkaEventPublisher;

        @Override
        public void run(String... args) {

                /*
                 * var event = DocumentUploadedEvent.newBuilder()
                 * .setDocumentId(8L)
                 * .setSubject("f46b0253-b104-44a5-af58-f4a5bd306d63")
                 * .setBucket("documents")
                 * .setObjectKey(
                 * "citizens/f46b0253-b104-44a5-af58-f4a5bd306d63/1c83626a-bbe0-4a68-beab-4722ca22a2cb-markdown-preview.pdf")
                 * .setDocumentType("PROOF_OF_ADDRESS")
                 * .setContentType("application/pdf")
                 * .setUploadedAt(Instant.now().toString())
                 * .build();
                 * 
                 * kafkaEventPublisher.publish("document.uploaded",
                 * "citizens/f46b0253-b104-44a5-af58-f4a5bd306d63/1c83626a-bbe0-4a68-beab-4722ca22a2cb-markdown-preview.pdf",
                 * event);
                 */

                /* */ String text = "DIRECTION GÉNÉRALE DES FINANCES PUBLIQUES\n" + //
                                "\n" + //
                                "Impôt sur les revenus de 2024\n" + //
                                "Avis d'impôt établi en 2025\n" + //
                                "\n" + //
                                "CENTRE DES FINANCES PUBLIQUES\n" + //
                                "SIP TOURCOING\n" + //
                                "SAID TOURCOING VILLE\n" + //
                                "PLACE DE LA RESISTANCE\n" + //
                                "59209 TOURCOING CEDEX\n" + //
                                "\n" + //
                                "Numéro fiscal :\n" + //
                                "04 49 824 910 297\n" + //
                                "\n" + //
                                "Déclarant 1 :\n" + //
                                "FOFANA MAMADOU\n" + //
                                "\n" + //
                                "Numéro fiscal déclarant 2 :\n" + //
                                "30 34 657 859 044\n" + //
                                "\n" + //
                                "Déclarant 2 :\n" + //
                                "DIALLO FATOUMATA BINTA\n" + //
                                "\n" + //
                                "Référence de l'avis :\n" + //
                                "25 59 A513124 77\n" + //
                                "\n" + //
                                "Adresse d'imposition :\n" + //
                                "89 RUE DE GUISNES\n" + //
                                "59200 TOURCOING\n" + //
                                "\n" + //
                                "Date d'établissement :\n" + //
                                "08/07/2025\n" + //
                                "\n" + //
                                "Date de mise en recouvrement :\n" + //
                                "31/07/2025\n" + //
                                "\n" + //
                                "Vous serez remboursé le :\n" + //
                                "25/07/2025\n" + //
                                "\n" + //
                                "Montant remboursé :\n" + //
                                "356,00 €\n" + //
                                "\n" + //
                                "IBAN :\n" + //
                                "FR76 3000 4000 420X XXXX XXX2 827\n" + //
                                "\n" + //
                                "BIC :\n" + //
                                "BNPAFRPPXXX\n" + //
                                "\n" + //
                                "Revenu fiscal de référence :\n" + //
                                "35 147\n" + //
                                "\n" + //
                                "Nombre de parts :\n" + //
                                "4,00\n" + //
                                "\n" + //
                                "====================\n" + //
                                "\n" + //
                                "Revenus\n" + //
                                "\n" + //
                                "Salaires :\n" + //
                                "37 755\n" + //
                                "\n" + //
                                "Déduction 10 % :\n" + //
                                "3 776\n" + //
                                "\n" + //
                                "Salaires nets :\n" + //
                                "33 979\n" + //
                                "\n" + //
                                "Revenus de capitaux mobiliers :\n" + //
                                "2 782\n" + //
                                "\n" + //
                                "Revenu brut global :\n" + //
                                "36 761\n" + //
                                "\n" + //
                                "CSG déductible :\n" + //
                                "189\n" + //
                                "\n" + //
                                "Pensions alimentaires :\n" + //
                                "3 500\n" + //
                                "\n" + //
                                "Revenu imposable :\n" + //
                                "33 072\n" + //
                                "\n" + //
                                "Impôt sur le revenu :\n" + //
                                "356\n" + //
                                "\n" + //
                                "Impôt net :\n" + //
                                "356\n" + //
                                "\n" + //
                                "Montant remboursé :\n" + //
                                "356,00 €\n" + //
                                "\n" + //
                                "Document :\n" + //
                                "Avis d'impôt sur les revenus de 2024\"\n" + //
                                "";

                var completed = DocumentOCRCompletedEvent.newBuilder()
                                .setDocumentId(8L)
                                .setSubject("f46b0253-b104-44a5-af58-f4a5bd306d63")
                                .setBucket("documents")
                                .setObjectKey("\"citizens/f46b0253-b104-44a5-af58-f4a5bd306d63/1c83626a-bbe0-4a68-beab-4722ca22a2cb")
                                .setDocumentType("TAX_NOTICE")
                                .setContentType("application/pdf")
                                .setText(text)
                                .setProcessedAt(Instant.now().toString())
                                .build();

                kafkaEventPublisher.publish(
                                "document.ocr.completed",
                                "citizens/f46b0253-b104-44a5-af58-f4a5bd306d63/1c83626a-bbe0-4a68-beab-4722ca22a2cb",
                                completed);

        }
}