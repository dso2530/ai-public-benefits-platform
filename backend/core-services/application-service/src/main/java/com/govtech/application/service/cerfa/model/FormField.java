package com.govtech.application.service.cerfa.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FormField(

                @JsonProperty("page") int pageNumber,

                @JsonProperty("label") String description,

                @JsonProperty("entry_bbox") BoundingBox entryBoundingBox,

                @JsonProperty("fieldKey") CerfaFieldKey fieldKey,

                @JsonProperty("entryText") EntryText entryText

) {
}