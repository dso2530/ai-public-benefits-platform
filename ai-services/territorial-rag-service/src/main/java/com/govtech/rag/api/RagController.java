
package com.govtech.rag.api;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.rag.api.dto.RagAnswerResponse;
import com.govtech.rag.api.dto.RagQueryRequest;
import com.govtech.rag.api.dto.RagSourceResponse;
import com.govtech.rag.application.usecase.AskTerritorialQuestionUseCase;
import com.govtech.rag.domain.model.Answer;
import com.govtech.rag.domain.model.Query;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rag")
public class RagController {

        private final AskTerritorialQuestionUseCase useCase;

        @PostMapping("/query")
        public RagAnswerResponse query(
                        @RequestBody RagQueryRequest request, @AuthenticationPrincipal Jwt jwt) {

                Query query = new Query(
                                request.question(),
                                request.territoryCode(),
                                request.source());

                Answer answer = useCase.ask(query);

                return new RagAnswerResponse(

                                answer.answer(),

                                answer.sources()
                                                .stream()
                                                .map(chunk -> new RagSourceResponse(
                                                                chunk.documentId(),

                                                                chunk.chunkNumber(),

                                                                chunk.score()))
                                                .toList());

        }

        @PostMapping(value = "/query/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
        public Flux<String> queryStream(
                        @RequestBody RagQueryRequest request,
                        @AuthenticationPrincipal Jwt jwt) {

                Query query = new Query(
                                request.question(),
                                request.territoryCode(),
                                request.source());

                return useCase.askStream(query);
        }

}