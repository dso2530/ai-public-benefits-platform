package com.govtech.application.service.cerfa;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.govtech.application.service.cerfa.model.CerfaTemplate;

import tools.jackson.databind.ObjectMapper;

@Service
public class CerfaTemplateLoader {

    private final ObjectMapper mapper;

    public CerfaTemplateLoader(
            ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public CerfaTemplate load(
            InputStream input) throws IOException {

        return mapper.readValue(
                input,
                CerfaTemplate.class);
    }
}