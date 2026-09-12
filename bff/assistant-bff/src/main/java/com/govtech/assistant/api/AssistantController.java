package com.govtech.assistant.api;

import org.springframework.web.bind.annotation.*;

import com.govtech.assistant.api.dto.*;
import com.govtech.assistant.service.AssistantService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/assistant")
@RequiredArgsConstructor
public class AssistantController {

    private final AssistantService service;

    @PostMapping("/query")
    public AssistantResponse query(

            @Valid @RequestBody AssistantQueryRequest request) {

        return service.ask(request);

    }

}