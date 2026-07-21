package com.govtech.application.service.cerfa.model;

import java.util.List;

public record CerfaTemplate(
        List<PageDefinition> pages,
        List<FormField> formFields) {
}