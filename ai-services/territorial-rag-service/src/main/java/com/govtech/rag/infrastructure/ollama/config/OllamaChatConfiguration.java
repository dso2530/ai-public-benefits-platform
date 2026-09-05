package com.govtech.rag.infrastructure.ollama.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OllamaChatConfiguration {

    @Bean
    ChatClient chatClient(ChatModel chatModel) {

        return ChatClient
                .builder(chatModel)
                .build();
    }

}
