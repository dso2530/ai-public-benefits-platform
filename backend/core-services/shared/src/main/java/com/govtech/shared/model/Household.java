package com.govtech.shared.model;

import com.govtech.shared.enums.MaritalStatus;

public record Household(MaritalStatus maritalStatus, int adults, int children) {}
