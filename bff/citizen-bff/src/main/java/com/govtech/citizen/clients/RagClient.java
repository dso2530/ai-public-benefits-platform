package com.govtech.citizen.clients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.govtech.citizen.assistant.dto.RagQueryRequest;
import com.govtech.citizen.assistant.dto.RagResponse;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class RagClient {

    private static final String SERVICE_NAME = "assistant-service";

    private final RestClient restClient;

    @Value("${services.assitant.url}")
    private String ragUrl;

    /**
     * Appel classique.
     *
     * Utilisé par CitizenApp et conserve le contrat
     * RagResponse.
     */
    public RagResponse query(RagQueryRequest request) {

        try {

            return restClient
                    .post()
                    .uri(ragUrl + "/api/rag/query")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(RagResponse.class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME);

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    /**
     * Appel streaming SSE.
     *
     * Le endpoint RAG envoie :
     *
     * data: token
     *
     * data: token
     *
     * ...
     */
    public Flux<String> queryStream(RagQueryRequest request) {

        return Flux.create(sink -> {

            try {

                restClient
                        .post()
                        .uri(ragUrl + "/api/rag/query/stream")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .body(request)
                        .exchange((clientRequest, clientResponse) -> {

                            HttpStatusCode status = clientResponse.getStatusCode();

                            /*
                             * exchange() ne déclenche pas automatiquement
                             * RestClientErrorHandler comme retrieve().
                             *
                             * Il faut donc vérifier explicitement le statut.
                             */
                            if (status.isError()) {

                                /*
                                 * On ne peut pas simplement créer un
                                 * RestClientResponseException ici sans
                                 * récupérer le contenu de la réponse.
                                 *
                                 * Pour conserver ton mécanisme actuel,
                                 * on transforme l'erreur HTTP en exception
                                 * applicative.
                                 */

                                if (status.value() == 404) {

                                    sink.error(
                                            new com.govtech.platform.web.error.ApplicationException(
                                                    com.govtech.platform.web.error.ErrorCode.RESOURCE_NOT_FOUND,
                                                    "Ressource introuvable dans le service "
                                                            + SERVICE_NAME));

                                } else if (status.is5xxServerError()) {

                                    sink.error(
                                            new ExternalServiceUnavailableException(
                                                    "Le service "
                                                            + SERVICE_NAME
                                                            + " est temporairement indisponible."));

                                } else {

                                    sink.error(
                                            new com.govtech.platform.web.error.ApplicationException(
                                                    com.govtech.platform.web.error.ErrorCode.EXTERNAL_SERVICE_ERROR,
                                                    "Le service "
                                                            + SERVICE_NAME
                                                            + " a rejeté la requête."));
                                }

                                return null;
                            }

                            try (
                                    BufferedReader reader = new BufferedReader(
                                            new InputStreamReader(
                                                    clientResponse.getBody(),
                                                    StandardCharsets.UTF_8))) {

                                String line;

                                while ((line = reader.readLine()) != null) {

                                    if (line.startsWith("data:")) {

                                        String data = line.substring(5).trim();

                                        if (!data.isEmpty()) {
                                            sink.next(data);
                                        }
                                    }
                                }

                                sink.complete();

                            } catch (Exception e) {

                                sink.error(
                                        new ExternalServiceUnavailableException(
                                                "Erreur pendant le streaming du service "
                                                        + SERVICE_NAME,
                                                e));
                            }

                            return null;
                        });

            } catch (ResourceAccessException e) {

                sink.error(
                        serviceUnavailable(e));

            } catch (Exception e) {

                sink.error(e);
            }
        });
    }

    private ExternalServiceUnavailableException serviceUnavailable(
            ResourceAccessException ex) {

        return new ExternalServiceUnavailableException(
                "Le service " + SERVICE_NAME
                        + " est indisponible.",
                ex);
    }
}
