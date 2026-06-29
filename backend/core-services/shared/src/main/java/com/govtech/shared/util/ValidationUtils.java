package com.govtech.shared.util;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}