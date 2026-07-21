package com.govtech.application.service.cerfa.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;

public record BoundingBox(
                float x0,
                float y0,
                float x1,
                float y1) {

        @JsonCreator
        public static BoundingBox fromJson(List<Float> values) {
                return new BoundingBox(
                                values.get(0),
                                values.get(1),
                                values.get(2),
                                values.get(3));
        }
}