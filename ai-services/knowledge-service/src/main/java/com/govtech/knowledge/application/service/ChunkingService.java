package com.govtech.knowledge.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.govtech.knowledge.domain.model.DocumentChunk;
import com.govtech.knowledge.infrastructure.config.KnowledgeProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChunkingService {

        private static final Pattern MULTIPLE_SPACES = Pattern.compile("[ \\t]+");

        /*
         * Découpe sur les fins de phrases.
         *
         * On garde le séparateur dans le texte grâce au lookbehind.
         */
        private static final Pattern SENTENCE_PATTERN = Pattern.compile("(?<=[.!?])\\s+");

        private final KnowledgeProperties properties;

        public void process(
                        Long documentId,
                        String text,
                        Map<String, String> metadata,
                        Consumer<DocumentChunk> consumer) {

                if (text == null || text.isBlank()) {
                        return;
                }

                int size = properties.getChunking().getSize();
                int overlap = properties.getChunking().getOverlap();

                if (size <= 0) {
                        throw new IllegalArgumentException(
                                        "Chunk size must be greater than 0");
                }

                if (overlap < 0 || overlap >= size) {
                        throw new IllegalArgumentException(
                                        "Chunk overlap must be >= 0 and < chunk size");
                }

                String normalized = normalize(text);

                List<String> paragraphs = splitParagraphs(normalized);

                List<String> segments = new ArrayList<>();

                for (String paragraph : paragraphs) {

                        if (paragraph.length() <= size) {
                                segments.add(paragraph);
                        } else {
                                /*
                                 * Un paragraphe trop long est découpé
                                 * intelligemment par phrases.
                                 */
                                segments.addAll(
                                                splitLongParagraph(
                                                                paragraph,
                                                                size));
                        }
                }

                buildChunks(
                                documentId,
                                segments,
                                metadata,
                                size,
                                overlap,
                                consumer);
        }

        /**
         * Normalisation du texte issu notamment de l'OCR.
         */
        private String normalize(String text) {

                return text
                                .replace("\r\n", "\n")
                                .replace('\r', '\n')

                                /*
                                 * Supprime les espaces en fin de ligne.
                                 */
                                .replaceAll("[ \\t]+\\n", "\n")

                                /*
                                 * Normalise les espaces horizontaux.
                                 */
                                .replaceAll("[ \\t]+", " ")

                                /*
                                 * Évite les dizaines de lignes vides
                                 * générées par certains OCR.
                                 */
                                .replaceAll("\\n{3,}", "\n\n")

                                .trim();
        }

        /**
         * Découpe d'abord sur les paragraphes.
         *
         * Si l'OCR ne produit aucun paragraphe,
         * le texte entier devient un seul segment
         * et sera ensuite découpé par phrases.
         */
        private List<String> splitParagraphs(String text) {

                List<String> paragraphs = new ArrayList<>();

                for (String paragraph : text.split("\\n\\s*\\n")) {

                        String cleaned = MULTIPLE_SPACES
                                        .matcher(paragraph)
                                        .replaceAll(" ")
                                        .trim();

                        if (!cleaned.isBlank()) {
                                paragraphs.add(cleaned);
                        }
                }

                /*
                 * Cas particulier : texte sans paragraphe.
                 *
                 * Exemple :
                 *
                 * "Bonjour ceci est un texte très long. Voici
                 * une deuxième phrase. Et encore une autre..."
                 */
                if (paragraphs.isEmpty() && !text.isBlank()) {
                        paragraphs.add(text.trim());
                }

                return paragraphs;
        }

        /**
         * Découpe un paragraphe trop long en conservant
         * autant que possible les frontières des phrases.
         */
        private List<String> splitLongParagraph(
                        String paragraph,
                        int size) {

                List<String> sentences = splitSentences(paragraph);

                /*
                 * Aucun découpage possible :
                 * on tombe sur un découpage par taille.
                 */
                if (sentences.size() <= 1) {
                        return splitBySize(paragraph, size);
                }

                List<String> result = new ArrayList<>();

                StringBuilder current = new StringBuilder();

                for (String sentence : sentences) {

                        sentence = sentence.trim();

                        if (sentence.isBlank()) {
                                continue;
                        }

                        /*
                         * Une phrase seule est déjà trop longue.
                         */
                        if (sentence.length() > size) {

                                if (!current.isEmpty()) {
                                        result.add(current.toString().trim());
                                        current.setLength(0);
                                }

                                result.addAll(
                                                splitBySize(
                                                                sentence,
                                                                size));

                                continue;
                        }

                        /*
                         * Ajouter la phrase au segment courant
                         * si elle tient.
                         */
                        if (current.length() > 0
                                        && current.length()
                                                        + 1
                                                        + sentence.length() > size) {

                                result.add(current.toString().trim());

                                current.setLength(0);
                        }

                        if (!current.isEmpty()) {
                                current.append(' ');
                        }

                        current.append(sentence);
                }

                if (!current.isEmpty()) {
                        result.add(current.toString().trim());
                }

                return result;
        }

        private List<String> splitSentences(String text) {

                List<String> sentences = new ArrayList<>();

                for (String sentence : SENTENCE_PATTERN.split(text)) {

                        String cleaned = sentence.trim();

                        if (!cleaned.isBlank()) {
                                sentences.add(cleaned);
                        }
                }

                return sentences;
        }

        /**
         * Fallback pour les textes sans ponctuation
         * ou les phrases extrêmement longues.
         */
        private List<String> splitBySize(
                        String text,
                        int size) {

                List<String> result = new ArrayList<>();

                int start = 0;

                while (start < text.length()) {

                        int end = Math.min(
                                        start + size,
                                        text.length());

                        /*
                         * On essaie de couper sur un espace
                         * plutôt qu'au milieu d'un mot.
                         */
                        if (end < text.length()) {

                                int space = text.lastIndexOf(
                                                ' ',
                                                end);

                                if (space > start) {
                                        end = space;
                                }
                        }

                        String chunk = text
                                        .substring(start, end)
                                        .trim();

                        if (!chunk.isBlank()) {
                                result.add(chunk);
                        }

                        if (end >= text.length()) {
                                break;
                        }

                        start = end;
                }

                return result;
        }

        /**
         * Construit les chunks finaux avec overlap.
         *
         * L'overlap est récupéré depuis la fin du chunk précédent.
         */
        private void buildChunks(
                        Long documentId,
                        List<String> segments,
                        Map<String, String> metadata,
                        int size,
                        int overlap,
                        Consumer<DocumentChunk> consumer) {

                if (segments.isEmpty()) {
                        return;
                }

                StringBuilder current = new StringBuilder();

                int chunkNumber = 0;

                for (String segment : segments) {

                        if (segment == null || segment.isBlank()) {
                                continue;
                        }

                        /*
                         * Le segment tient dans le chunk courant.
                         */
                        if (current.length() == 0) {

                                current.append(segment);

                                continue;
                        }

                        if (current.length()
                                        + 1
                                        + segment.length() <= size) {

                                current
                                                .append(' ')
                                                .append(segment);

                                continue;
                        }

                        /*
                         * Chunk terminé.
                         */
                        String content = current
                                        .toString()
                                        .trim();

                        if (!content.isBlank()) {

                                consumer.accept(
                                                new DocumentChunk(
                                                                documentId,
                                                                chunkNumber++,
                                                                content,
                                                                metadata));
                        }

                        /*
                         * Création de l'overlap.
                         */
                        String overlapText = createOverlap(
                                        content,
                                        overlap);

                        current = new StringBuilder();

                        if (!overlapText.isBlank()) {
                                current.append(overlapText);
                        }

                        /*
                         * Le nouveau segment peut être ajouté.
                         *
                         * Si l'overlap + segment dépasse la taille,
                         * le segment est ajouté seul.
                         */
                        if (current.length() == 0) {

                                current.append(segment);

                        } else if (current.length()
                                        + 1
                                        + segment.length() <= size) {

                                current
                                                .append(' ')
                                                .append(segment);

                        } else {

                                /*
                                 * L'overlap lui-même prend trop de place.
                                 * On repart avec le nouveau segment.
                                 */
                                current.setLength(0);
                                current.append(segment);
                        }
                }

                /*
                 * Dernier chunk.
                 */
                if (!current.isEmpty()) {

                        String content = current
                                        .toString()
                                        .trim();

                        if (!content.isBlank()) {

                                consumer.accept(
                                                new DocumentChunk(
                                                                documentId,
                                                                chunkNumber,
                                                                content,
                                                                metadata));
                        }
                }
        }

        private String createOverlap(
                        String content,
                        int overlap) {

                if (overlap <= 0
                                || content.length() <= overlap) {

                        return content;
                }

                int start = content.length() - overlap;

                /*
                 * On évite autant que possible de commencer
                 * au milieu d'un mot.
                 */
                int space = content.indexOf(
                                ' ',
                                start);

                if (space >= 0
                                && space < content.length() - 1) {

                        start = space + 1;
                }

                return content
                                .substring(start)
                                .trim();
        }
}