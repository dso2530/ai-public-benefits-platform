package com.govtech.apply.application.cerfa.model;

import java.util.List;

public record CerfaTemplate(
        List<PageDefinition> pages,
        List<FormField> formFields) {
}