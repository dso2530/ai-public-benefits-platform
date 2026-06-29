package com.govtech.shared.util;

import java.time.LocalDate;
import java.time.Period;

public final class DateUtils {

    private DateUtils() {
    }

    public static int age(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}