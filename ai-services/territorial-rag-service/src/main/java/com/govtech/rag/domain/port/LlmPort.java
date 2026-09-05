package com.govtech.rag.domain.port;

import reactor.core.publisher.Flux;

public interface LlmPort {

    String generate(
            String prompt);

    Flux<String> generateStream(String prompt);

}