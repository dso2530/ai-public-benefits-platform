
package com.govtech.rag.application.usecase;

import com.govtech.rag.domain.model.Answer;
import com.govtech.rag.domain.model.Query;

import reactor.core.publisher.Flux;

public interface AskTerritorialQuestionUseCase {

    Answer ask(
            Query query);

    Flux<String> askStream(Query query);

}