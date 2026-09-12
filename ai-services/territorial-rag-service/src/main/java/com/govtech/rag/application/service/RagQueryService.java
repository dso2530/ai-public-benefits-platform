package com.govtech.rag.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.rag.application.usecase.AskTerritorialQuestionUseCase;
import com.govtech.rag.domain.model.Answer;
import com.govtech.rag.domain.model.Query;
import com.govtech.rag.domain.model.RetrievedChunk;
import com.govtech.rag.domain.port.EmbeddingPort;
import com.govtech.rag.domain.port.KnowledgeSearchPort;
import com.govtech.rag.domain.port.LlmPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
@Slf4j
public class RagQueryService
    implements AskTerritorialQuestionUseCase {

  private final EmbeddingPort embeddingPort;
  private final KnowledgeSearchPort knowledgeSearchPort;
  private final ContextBuilder contextBuilder;
  private final LlmPort llmPort;

  @Override
  public Answer ask(Query query) {

    long start = System.currentTimeMillis();

    log.info(
        "Processing RAG query territory={}, question='{}'",
        query.territoryCode(),
        query.question());

    // ============================================================
    // 1. EMBEDDING
    // ============================================================

    float[] embedding = embeddingPort.embed(query.question());

    // ============================================================
    // 2. RETRIEVAL
    // ============================================================

    List<RetrievedChunk> chunks = knowledgeSearchPort.search(
        embedding,
        query.territoryCode(),
        6);

    log.info(
        "Knowledge service returned {} chunks for territory={}",
        chunks.size(),
        query.territoryCode());

    // ============================================================
    // 3. NO RESULT
    // ============================================================

    if (chunks.isEmpty()) {

      log.warn(
          "No knowledge found for territory={}, question='{}'",
          query.territoryCode(),
          query.question());

      return new Answer(
          """
              {
                "prestations": [],
                "message": "Les informations disponibles ne permettent pas de répondre précisément à cette question."
              }
              """,
          List.of(),
          0L);
    }

    // ============================================================
    // 4. BUILD CONTEXT
    // ============================================================

    String context = contextBuilder.build(chunks);

    log.info(
        "RAG context built: chunks={}, contextChars={}",
        chunks.size(),
        context.length());

    // ============================================================
    // 5. BUILD PROMPT
    // ============================================================

    String prompt = buildPrompt(query, context);

    log.info(
        "Sending structured RAG prompt to LLM: territory={}, contextChars={}, promptChars={}",
        query.territoryCode(),
        context.length(),
        prompt.length());

    // ============================================================
    // 6. LLM
    // ============================================================

    log.info(
        "RAG prompt: chunks={}, contextChars={}, promptChars={}",
        chunks.size(),
        context.length(),
        prompt.length());

    String rawResponse = llmPort.generate(prompt);

    log.info(
        "LLM generated response ({} characters)",
        rawResponse.length());

    // ============================================================
    // 7. VALIDATE JSON
    // ============================================================

    String response = normalizeJsonResponse(rawResponse);

    long processingTimeMs = System.currentTimeMillis() - start;

    // ============================================================
    // 8. RETURN
    // ============================================================

    return new Answer(
        response,
        chunks,
        processingTimeMs);
  }

  @Override
  public Flux<String> askStream(Query query) {

    log.info(
        "Processing streaming RAG query territory={}, question='{}'",
        query.territoryCode(),
        query.question());

    // ============================================================
    // 1. RETRIEVAL
    // ============================================================

    float[] embedding = embeddingPort.embed(query.question());

    List<RetrievedChunk> chunks = knowledgeSearchPort.search(
        embedding,
        query.territoryCode(),
        8);

    log.info(
        "Knowledge service returned {} chunks for streaming query",
        chunks.size());

    // ============================================================
    // 2. NO CONTEXT
    // ============================================================

    if (chunks.isEmpty()) {

      return Flux.just(
          """
              {
                "prestations": [],
                "message": "Les informations disponibles ne permettent pas de répondre précisément à cette question."
              }
              """);
    }

    // ============================================================
    // 3. CONTEXT
    // ============================================================

    String context = contextBuilder.build(chunks);

    // ============================================================
    // 4. PROMPT
    // ============================================================

    String prompt = buildPrompt(query, context);

    log.info(
        "Starting LLM streaming: territory={}, contextChars={}, promptChars={}",
        query.territoryCode(),
        context.length(),
        prompt.length());

    // ============================================================
    // 5. STREAM
    // ============================================================
    log.info(
        "RAG prompt: chunks={}, contextChars={}, promptChars={}",
        chunks.size(),
        context.length(),
        prompt.length());
    return llmPort.generateStream(prompt)
        .doOnSubscribe(
            subscription -> log.info("LLM stream started"))
        .doOnComplete(
            () -> log.info("LLM stream completed"))
        .doOnError(
            error -> log.error(
                "LLM stream failed",
                error));
  }

  // ================================================================
  // PROMPT STRUCTURÉ
  // ================================================================

  private String buildPrompt(Query query, String context) {

    return """
        Tu es un assistant spécialisé dans les aides publiques françaises.

        Analyse la QUESTION à partir des règles et prestations présentes
        dans le CONTEXTE.

        RÈGLES :

        - Les faits concernant l'utilisateur viennent UNIQUEMENT de la QUESTION.
        - Les prestations et leurs conditions viennent UNIQUEMENT du CONTEXTE.
        - Une condition du CONTEXTE n'est jamais un fait concernant l'utilisateur.
        - Une information absente de la QUESTION est inconnue.
        - Ne jamais inventer une information personnelle.
        - Ne jamais inventer une prestation, un montant, une condition ou une date.
        - Répondre dès qu'une prestation du CONTEXTE semble pertinente.
        - Si des informations nécessaires manquent, les indiquer dans
          "informations_manquantes".
        - Ne jamais affirmer une éligibilité certaine si les informations
          disponibles ne permettent pas de la confirmer.

        STATUTS :

        "POTENTIELLEMENT_ELIGIBLE"
        → La situation connue correspond à certains critères de la prestation,
          mais des informations peuvent encore manquer.

        "A_VERIFIER"
        → La prestation semble pertinente mais des informations importantes
          manquent pour déterminer si elle s'applique.

        "NON_ELIGIBLE"
        → Utiliser uniquement si le CONTEXTE permet clairement de conclure
          que la situation ne remplit pas les conditions.

        EXEMPLE :

        QUESTION :
        "Je suis en couple et j'ai 3 enfants."

        Si le CONTEXTE indique que les allocations familiales concernent
        les familles ayant plusieurs enfants, propose cette prestation.

        Si le CONTEXTE indique également que certaines conditions dépendent
        de l'âge des enfants ou des revenus, ces informations doivent être
        demandées si elles ne sont pas présentes dans la QUESTION.

        Ne jamais inventer l'âge des enfants ou les revenus.

        FORMAT :

        Retourne UNIQUEMENT un JSON valide.
        Aucun markdown.
        Aucun texte avant ou après le JSON.

        {
          "prestations": [
            {
              "code": "AF",
              "nom": "Allocations familiales",
              "statut": "POTENTIELLEMENT_ELIGIBLE",
              "raison": "Explication courte fondée sur la QUESTION et le CONTEXTE.",
              "informations_manquantes": [
                "revenus du foyer"
              ]
            }
          ]
        }

        S'il n'existe aucune prestation pertinente dans le CONTEXTE :

        {
          "prestations": [],
          "message": "Aucune prestation pertinente n'a pu être identifiée dans les informations disponibles."
        }

        TERRITOIRE :
        %s

        CONTEXTE :
        <context>
        %s
        </context>

        QUESTION :
        <question>
        %s
        </question>

        Retourne uniquement le JSON.
        """
        .formatted(
            query.territoryCode(),
            context,
            query.question());
  }

  // ================================================================
  // JSON NORMALIZATION
  // ================================================================

  private String normalizeJsonResponse(String rawResponse) {

    if (rawResponse == null || rawResponse.isBlank()) {
      return fallbackJson();
    }

    String response = rawResponse.trim();

    // Qwen peut éventuellement entourer le JSON avec ```json ... ```
    if (response.startsWith("```json")) {
      response = response.substring("```json".length()).trim();
    } else if (response.startsWith("```")) {
      response = response.substring("```".length()).trim();
    }

    if (response.endsWith("```")) {
      response = response.substring(
          0,
          response.length() - "```".length()).trim();
    }

    // Vérification minimale
    if (!response.startsWith("{") || !response.endsWith("}")) {
      log.warn("LLM returned something that does not look like JSON: {}", response);
      return fallbackJson();
    }

    if (!response.contains("\"prestations\"")) {
      log.warn("LLM JSON does not contain 'prestations': {}", response);
      return fallbackJson();
    }

    return response;
  }

  private String fallbackJson() {

    return """
        {
          "prestations": [],
          "message": "Les informations disponibles ne permettent pas de répondre précisément à cette question."
        }
        """;
  }
}