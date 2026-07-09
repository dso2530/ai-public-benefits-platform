package com.govtech.profile.application.mapper;

import tools.jackson.databind.ObjectMapper;
import com.govtech.profile.application.dto.ExtractedData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtractionDataMapper {

    private final ObjectMapper objectMapper;

    public ExtractedData from(String json) {
        try {
            return objectMapper.readValue(json, ExtractedData.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid extracted data", e);
        }
    }
}