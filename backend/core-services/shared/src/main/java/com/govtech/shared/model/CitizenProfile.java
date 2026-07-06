package com.govtech.shared.model;

import java.time.LocalDate;
import java.util.List;

public record CitizenProfile(
    String nationalId,
    String firstName,
    String lastName,
    LocalDate birthDate,
    Household household,
    Income income,
    TaxNotice taxNotice,
    List<String> eligibleBenefits) {}
